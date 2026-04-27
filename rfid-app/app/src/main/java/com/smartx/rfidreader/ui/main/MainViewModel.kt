package com.smartx.rfidreader.ui.main

import android.app.Application
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.smartx.rfidreader.RfidApplication
import com.smartx.rfidreader.core.location.GpsHelper
import com.smartx.rfidreader.core.reader.IRfidReader
import com.smartx.rfidreader.core.reader.ReaderConfig
import com.smartx.rfidreader.core.reader.ReaderConnectionState
import com.smartx.rfidreader.core.reader.RfidTag
import com.smartx.rfidreader.core.registry.ReaderRegistry
import com.smartx.rfidreader.core.settings.AppSettings
import com.smartx.rfidreader.core.settings.AppSettingsRepository
import com.smartx.rfidreader.readers.x714.X714Reader
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class MainUiState(
    val connectionState: ReaderConnectionState = ReaderConnectionState.DISCONNECTED,
    val isConnecting: Boolean = false,
    val isInventorying: Boolean = false,
    val config: ReaderConfig = ReaderConfig(),
    val isSavingConfig: Boolean = false,
    val configSaveSuccess: Boolean? = null,
    val appSettings: AppSettings = AppSettings(),
    val errorMessage: String? = null
)

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val TAG = "MainViewModel"
    private val settingsRepo = AppSettingsRepository(app)

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    /** Armazenamento rápido para lookup O(1) por EPC */
    private val _tagMap = LinkedHashMap<String, RfidTag>()

    /** Flag para atualizar a UI apenas quando necessário (throttle de 200 ms) */
    @Volatile private var _tagsDirty = false

    /** Timestamp do último buzzer (cooldown de 300 ms) */
    @Volatile private var _lastBuzzerMs = 0L



    private val _tags = MutableStateFlow<List<RfidTag>>(emptyList())
    val tags: StateFlow<List<RfidTag>> = _tags.asStateFlow()

    /** Limite de tags exibidas na tela (null = todas). Padrão: 50 */
    private val _displayLimit = MutableStateFlow<Int?>(50)
    val displayLimit: StateFlow<Int?> = _displayLimit.asStateFlow()

    fun setDisplayLimit(limit: Int?) {
        _displayLimit.value = limit
    }

    /** Evento de buzzer: emitido a cada nova tag detectada (cooldown 300 ms) */
    private val _buzzerEvent = MutableSharedFlow<Unit>(extraBufferCapacity = 8)
    val buzzerEvent: SharedFlow<Unit> = _buzzerEvent.asSharedFlow()

    /**
     * Emite cada tag individual após passar pelos filtros de prefixo e RSSI.
     * Consumível por múltiplos collectors (SharedFlow). Usado pelo RadarFragment
     * para atualizar RSSI dos targets sem coletar tagFlow diretamente do hardware.
     */
    private val _tagEventFlow = MutableSharedFlow<RfidTag>(extraBufferCapacity = 64)
    val tagEventFlow: SharedFlow<RfidTag> = _tagEventFlow.asSharedFlow()

    /** Evento de navegação para a tela de leitura após conexão bem-sucedida */
    private val _navigateToReading = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val navigateToReading: SharedFlow<Unit> = _navigateToReading.asSharedFlow()

    /** Resultado ao salvar inventário: true = sucesso, false = erro */
    private val _saveInventoryResult = MutableSharedFlow<Boolean>(extraBufferCapacity = 1)
    val saveInventoryResult: SharedFlow<Boolean> = _saveInventoryResult.asSharedFlow()

    /** Log de conexão — lista acumulada de linhas exibida no ConnectionLogDialogFragment */
    private val _connectionLog = MutableStateFlow<List<String>>(emptyList())
    val connectionLog: StateFlow<List<String>> = _connectionLog.asStateFlow()
    private var connectionStartMs: Long = 0L

    private fun emitLog(msg: String) {
        val elapsed = (System.currentTimeMillis() - connectionStartMs) / 1000.0
        val line = "[+%1$.2fs] $msg".format(elapsed)
        _connectionLog.update { it + line }
    }

    val availableReaders: List<IRfidReader> = ReaderRegistry.availableReaders

    var reader: IRfidReader? = null
        private set

    init {
        viewModelScope.launch {
            settingsRepo.flow.collect { settings ->
                _uiState.update { it.copy(appSettings = settings) }
            }
        }
    }

    // -------------------------------------------------------------------------
    // Conexão
    // -------------------------------------------------------------------------

    /**
     * Tenta conectar automaticamente ao último leitor usado.
     * Aguarda o carregamento inicial das settings antes de decidir.
     * Não faz nada se já conectado, conectando, ou nenhum ID salvo.
     */
    fun autoConnectLastReader() {
        val state = _uiState.value
        if (state.connectionState == ReaderConnectionState.CONNECTED || state.isConnecting) return
        viewModelScope.launch {
            // Aguarda o primeiro valor válido de settings (pode ainda não ter chegado no init)
            val lastId = settingsRepo.flow.first().lastReaderId.ifBlank { return@launch }
            val rfidReader = ReaderRegistry.findById(lastId) ?: return@launch
            // Verifica novamente após await para evitar dupla conexão
            if (_uiState.value.connectionState == ReaderConnectionState.CONNECTED ||
                _uiState.value.isConnecting) return@launch
            connect(rfidReader)
        }
    }

    fun connect(rfidReader: IRfidReader) {
        if (_uiState.value.isConnecting) return
        viewModelScope.launch {
            connectionStartMs = System.currentTimeMillis()
            _connectionLog.value = emptyList()
            emitLog("Iniciando conexão com ${rfidReader.displayName}...")
            _uiState.update { it.copy(isConnecting = true, errorMessage = null) }
            Log.i(TAG, "connect start reader=${rfidReader.readerId} t=$connectionStartMs")

            // Conecta logSink para receber eventos BLE internos do X714
            (rfidReader as? X714Reader)?.logSink = { msg -> emitLog(msg) }

            val ok = rfidReader.connect(getApplication())
            val elapsed = System.currentTimeMillis() - connectionStartMs
            Log.i(TAG, "connect end reader=${rfidReader.readerId} ok=$ok total=${elapsed}ms")

            if (ok) {
                reader = rfidReader
                // Marca explicitamente como conectado — o collector em observeReader pode demorar
                _uiState.update {
                    it.copy(
                        isConnecting = false,
                        connectionState = ReaderConnectionState.CONNECTED
                    )
                }
                emitLog("Lendo configuração do leitor...")
                val settings = _uiState.value.appSettings
                settingsRepo.save(settings.copy(lastReaderId = rfidReader.readerId))
                observeReader(rfidReader)
                // Lê config atual e aplica imediatamente para garantir modo correto (ex: EPC+TID)
                val config = rfidReader.readConfig()
                rfidReader.applyConfig(config)
                _uiState.update { it.copy(config = config) }
                emitLog("✓ Pronto! (%.1fs)".format(elapsed / 1000.0))
                _navigateToReading.emit(Unit)
            } else {
                emitLog("✗ Falha ao conectar. Verifique se o leitor está ligado e próximo.")
                _uiState.update {
                    it.copy(isConnecting = false, errorMessage = "Falha ao conectar ${rfidReader.displayName}")
                }
            }
            // Limpa logSink para evitar referência ao ViewModel
            (rfidReader as? X714Reader)?.logSink = null
        }
    }

    fun disconnect() {
        viewModelScope.launch {
            if (reader?.isInventorying() == true) reader?.stopInventory()
            reader?.disconnect()
            reader = null
            _tags.value = emptyList()
            _uiState.update {
                it.copy(connectionState = ReaderConnectionState.DISCONNECTED, isInventorying = false)
            }
        }
    }

    private fun observeReader(r: IRfidReader) {
        viewModelScope.launch {
            r.connectionState.collect { state ->
                _uiState.update { it.copy(connectionState = state, isConnecting = false) }
                if (state == ReaderConnectionState.DISCONNECTED) {
                    _uiState.update { it.copy(isInventorying = false) }
                }
            }
        }
        // Observa mudanças de estado de inventário acionadas via GPI (botão físico BLE).
        // Necessário pois o GPI chega por BLE diretamente no X714Reader, sem passar pelo ViewModel.
        (r as? X714Reader)?.let { x714 ->
            viewModelScope.launch {
                x714.inventoryStateFlow.collect { isOn ->
                    _uiState.update { it.copy(isInventorying = isOn) }
                }
            }
        }
        viewModelScope.launch {
            r.tagFlow.collect { tag ->
                val settings = _uiState.value.appSettings

                // Filtro de prefixo
                val prefixes = settings.prefixes
                if (prefixes.isNotEmpty() && prefixes.none { tag.epc.startsWith(it, ignoreCase = true) }) {
                    return@collect
                }

                // Filtro RSSI
                val rssiVal = tag.rssi.toDoubleOrNull()
                if (rssiVal != null && rssiVal < settings.rssiFilter) return@collect

                val existing = _tagMap[tag.epc]
                val isNew = existing == null
                if (isNew) {
                    _tagMap[tag.epc] = tag
                } else {
                    _tagMap[tag.epc] = existing!!.copy(
                        readCount = existing.readCount + 1,
                        rssi = tag.rssi,
                        timestamp = tag.timestamp
                    )
                }

                // Marca dirty — a UI será atualizada pelo ticker de 200 ms
                _tagsDirty = true

                // Emite o evento individual para o radar (SharedFlow — safe para múltiplos collectors)
                _tagEventFlow.tryEmit(tag)

                // Buzzer para qualquer tag lida, com cooldown de 50 ms
                if (settings.buzzerEnabled) {
                    val now = System.currentTimeMillis()
                    if (now - _lastBuzzerMs >= 100L) {
                        _lastBuzzerMs = now
                        _buzzerEvent.tryEmit(Unit)
                    }
                }
            }
        }
        // Ticker: atualiza a lista na UI no máximo a cada 200 ms
        viewModelScope.launch {
            while (true) {
                kotlinx.coroutines.delay(200)
                if (_tagsDirty) {
                    _tagsDirty = false
                    _tags.value = _tagMap.values.toList()
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    // -------------------------------------------------------------------------
    // Inventário
    // -------------------------------------------------------------------------

    fun toggleInventory() {
        val r = reader ?: return
        if (r.isInventorying()) {
            r.stopInventory()
            _uiState.update { it.copy(isInventorying = false) }
        } else {
            val ok = r.startInventory()
            _uiState.update { it.copy(isInventorying = ok) }
        }
    }

    fun clearTags() {
        _tagMap.clear()
        _tagsDirty = false
        _tags.value = emptyList()
    }

    fun stopInventoryAndClear() {
        val r = reader ?: return
        if (r.isInventorying()) {
            r.stopInventory()
            _uiState.update { it.copy(isInventorying = false) }
        }
        _tagMap.clear()
        _tagsDirty = false
        _tags.value = emptyList()
    }

    fun onTriggerPressed() {
        val r = reader ?: return
        r.onTriggerPressed()
        _uiState.update { it.copy(isInventorying = r.isInventorying()) }
    }

    fun onTriggerReleased() {
        val r = reader ?: return
        r.onTriggerReleased()
        _uiState.update { it.copy(isInventorying = r.isInventorying()) }
    }

    // -------------------------------------------------------------------------
    // Configuração do leitor
    // -------------------------------------------------------------------------

    fun loadConfig() {
        val r = reader ?: return
        viewModelScope.launch {
            val config = r.readConfig()
            _uiState.update { it.copy(config = config) }
        }
    }

    fun saveConfig(config: ReaderConfig) {
        val r = reader ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isSavingConfig = true, configSaveSuccess = null) }
            val ok = r.applyConfig(config)
            _uiState.update {
                it.copy(
                    isSavingConfig = false,
                    configSaveSuccess = ok,
                    config = if (ok) config else it.config
                )
            }
        }
    }

    fun consumeConfigSaveResult() {
        _uiState.update { it.copy(configSaveSuccess = null) }
    }

    /**
     * Aplica Session 0 ao entrar no modo radar (sem alterar a config salva no estado).
     * Session 0 garante que todas as tags respondam sequencialmente na mesma sessão,
     * ideal para localização/radar.
     */
    fun enterRadarMode() {
        val r = reader ?: return
        val current = _uiState.value.config
        viewModelScope.launch { r.applyConfig(current.copy(session = 0)) }
    }

    /**
     * Restaura a session original configurada pelo usuário ao sair do modo radar.
     */
    fun exitRadarMode() {
        val r = reader ?: return
        val current = _uiState.value.config
        viewModelScope.launch { r.applyConfig(current) }
    }

    // -------------------------------------------------------------------------
    // Salvar inventário
    // -------------------------------------------------------------------------

    fun saveInventory() {
        val tags = _tags.value
        if (tags.isEmpty()) return
        viewModelScope.launch {
            try {
                val app = getApplication<RfidApplication>()
                val deviceId = Settings.Secure.getString(
                    app.contentResolver, Settings.Secure.ANDROID_ID
                ) ?: "unknown"
                val gps = GpsHelper(app).getLocation()
                val config = _uiState.value.config
                val appSettings = _uiState.value.appSettings
                app.eventRepository.saveInventory(
                    deviceId = deviceId,
                    tags = tags,
                    gpsLat = gps?.first ?: 0.0,
                    gpsLng = gps?.second ?: 0.0,
                    hasGps = gps != null,
                    txPower = config.txPower,
                    session = config.session,
                    rssiFilter = config.rssiFilter,
                    prefixes = appSettings.prefixes
                )
                _saveInventoryResult.emit(true)
            } catch (e: Exception) {
                Log.e(TAG, "Erro ao salvar inventário", e)
                _saveInventoryResult.emit(false)
            }
        }
    }

    // -------------------------------------------------------------------------
    // Configurações do app (persistentes via DataStore)
    // -------------------------------------------------------------------------

    fun saveAppSettings(settings: AppSettings) {
        viewModelScope.launch {
            val toSave = settings.copy(
                lastReaderId = reader?.readerId ?: _uiState.value.appSettings.lastReaderId
            )
            settingsRepo.save(toSave)
            _uiState.update { it.copy(appSettings = toSave) }
        }
    }

    // -------------------------------------------------------------------------
    // Lifecycle
    // -------------------------------------------------------------------------

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch { reader?.disconnect() }
    }
}
