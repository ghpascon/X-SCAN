package com.smartx.xscan.ui.main

import android.app.Application
import android.provider.Settings
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.smartx.rfid.core.ConnectionRequest
import com.smartx.rfid.core.IRfidReader
import com.smartx.rfid.core.ReaderConfig
import com.smartx.rfid.core.ReaderConnectionState
import com.smartx.rfid.core.RfidTag
import com.smartx.rfid.manager.ReaderManager
import com.smartx.rfid.tags.TagFilter
import com.smartx.xscan.RfidApplication
import com.smartx.xscan.core.location.GpsHelper
import com.smartx.xscan.core.settings.AppSettings
import com.smartx.xscan.core.settings.AppSettingsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
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

/**
 * Ponte entre a UI e o [ReaderManager] da biblioteca smartx-rfid.
 * A conexão, o inventário e as tags vivem no manager (compartilhado por todo o app);
 * aqui ficam apenas as regras de tela: configurações do app, buzzer e navegação.
 */
class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val TAG = "MainViewModel"
    private val manager: ReaderManager = (app as RfidApplication).readerManager
    private val settingsRepo = AppSettingsRepository(app)

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    /** Tags coletadas (já filtradas e deduplicadas pela biblioteca; atualizadas a cada ~200 ms). */
    val tags: StateFlow<List<RfidTag>> = manager.tags.tags

    /** Cada leitura individual que passou pelos filtros (usado pelo radar). */
    val tagEventFlow: SharedFlow<RfidTag> = manager.tags.events

    /** Log de conexão exibido no ConnectionLogDialogFragment. */
    val connectionLog: StateFlow<List<String>> = manager.connectionLog

    /** Leitores habilitados (config.json -> readers). */
    val availableReaders: List<IRfidReader> = manager.readers

    /** Leitor atualmente conectado. */
    val reader: IRfidReader? get() = manager.activeReader

    /** Limite de tags exibidas na tela (null = todas). Padrão: 50 */
    private val _displayLimit = MutableStateFlow<Int?>(50)
    val displayLimit: StateFlow<Int?> = _displayLimit.asStateFlow()

    fun setDisplayLimit(limit: Int?) {
        _displayLimit.value = limit
    }

    /** Evento de buzzer: emitido a cada tag lida (cooldown de 100 ms) */
    private val _buzzerEvent = MutableSharedFlow<Unit>(extraBufferCapacity = 8)
    val buzzerEvent: SharedFlow<Unit> = _buzzerEvent.asSharedFlow()
    @Volatile private var lastBuzzerMs = 0L

    /** Evento de navegação para a tela de leitura após conexão bem-sucedida */
    private val _navigateToReading = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val navigateToReading: SharedFlow<Unit> = _navigateToReading.asSharedFlow()

    /** Resultado ao salvar inventário: true = sucesso, false = erro */
    private val _saveInventoryResult = MutableSharedFlow<Boolean>(extraBufferCapacity = 1)
    val saveInventoryResult: SharedFlow<Boolean> = _saveInventoryResult.asSharedFlow()

    // Eventos para instruir a UI a abrir o modal de scan BLE (readerId) e o diálogo de logs
    private val _showBleScanDialog = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val showBleScanDialog: SharedFlow<String> = _showBleScanDialog.asSharedFlow()
    private val _showConnectionLog = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val showConnectionLog: SharedFlow<Unit> = _showConnectionLog.asSharedFlow()

    private var connectJob: Job? = null

    init {
        viewModelScope.launch {
            settingsRepo.flow.collect { settings ->
                manager.tags.filter = TagFilter(prefixes = settings.prefixes, minRssi = settings.rssiFilter)
                _uiState.update { it.copy(appSettings = settings) }
            }
        }
        viewModelScope.launch {
            manager.state.collect { s ->
                _uiState.update {
                    it.copy(
                        connectionState = s.connectionState,
                        isConnecting = s.isConnecting,
                        isInventorying = s.isInventorying,
                        config = s.config,
                        errorMessage = s.lastError
                    )
                }
            }
        }
        viewModelScope.launch {
            manager.tags.events.collect {
                if (_uiState.value.appSettings.buzzerEnabled) {
                    val now = System.currentTimeMillis()
                    if (now - lastBuzzerMs >= 100L) {
                        lastBuzzerMs = now
                        _buzzerEvent.tryEmit(Unit)
                    }
                }
            }
        }
    }

    // -------------------------------------------------------------------------
    // Conexão
    // -------------------------------------------------------------------------

    /**
     * Tenta conectar automaticamente ao último leitor usado.
     * Não faz nada se já conectado/conectando, se não há ID salvo, ou se o leitor
     * exige escolha explícita do usuário (`capabilities.supportsAutoReconnect = false`).
     */
    fun autoConnectLastReader() {
        val state = manager.state.value
        if (state.isConnected || state.isConnecting) return
        viewModelScope.launch {
            val settings = settingsRepo.flow.first()
            val lastId = settings.lastReaderId.ifBlank { return@launch }
            val rfidReader = manager.findById(lastId) ?: return@launch
            if (!rfidReader.capabilities.supportsAutoReconnect) return@launch

            var request = ConnectionRequest()
            if (rfidReader.isBle) {
                // Pede à UI o modal de scan BLE e o log para o usuário acompanhar
                _showBleScanDialog.tryEmit(rfidReader.readerId)
                _showConnectionLog.tryEmit(Unit)
                // Sem MAC salvo não tentamos reconectar
                val mac = settings.lastBleAddress.ifBlank { null } ?: return@launch
                request = ConnectionRequest(address = mac)
            }

            // Verifica novamente após o await para evitar dupla conexão
            val current = manager.state.value
            if (current.isConnected || current.isConnecting) return@launch
            connect(rfidReader, request)
        }
    }

    fun cancelConnection() {
        connectJob?.cancel()
        connectJob = null
        manager.clearError()
    }

    fun connect(rfidReader: IRfidReader, request: ConnectionRequest = ConnectionRequest()) {
        if (manager.state.value.isConnecting) return
        connectJob = viewModelScope.launch {
            val ok = manager.connect(rfidReader, request)
            if (ok) {
                // Persiste leitor (e MAC, se BLE) para permitir auto-reconexão
                val settings = _uiState.value.appSettings
                settingsRepo.save(
                    settings.copy(
                        lastReaderId = rfidReader.readerId,
                        lastBleAddress = rfidReader.targetMacAddress ?: settings.lastBleAddress
                    )
                )
                _navigateToReading.emit(Unit)
            }
            connectJob = null
        }
    }

    fun disconnect() {
        viewModelScope.launch { manager.disconnect() }
    }

    fun clearError() = manager.clearError()

    // -------------------------------------------------------------------------
    // Inventário
    // -------------------------------------------------------------------------

    fun toggleInventory() {
        manager.toggleInventory()
    }

    fun clearTags() = manager.clearTags()

    fun stopInventory() = manager.stopInventory()

    fun stopInventoryAndClear() {
        manager.stopInventory()
        manager.clearTags()
    }

    fun onTriggerPressed() {
        manager.onTriggerPressed()
    }

    fun onTriggerReleased() {
        manager.onTriggerReleased()
    }

    // -------------------------------------------------------------------------
    // Configuração do leitor
    // -------------------------------------------------------------------------

    fun loadConfig() {
        viewModelScope.launch { manager.readConfig() }
    }

    fun saveConfig(config: ReaderConfig) {
        if (reader == null) return
        viewModelScope.launch {
            _uiState.update { it.copy(isSavingConfig = true, configSaveSuccess = null) }
            val ok = manager.applyConfig(config)
            _uiState.update { it.copy(isSavingConfig = false, configSaveSuccess = ok) }
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
        val current = _uiState.value.config
        viewModelScope.launch { manager.applyConfig(current.copy(session = 0), remember = false) }
    }

    /** Restaura a session original configurada pelo usuário ao sair do modo radar. */
    fun exitRadarMode() {
        val current = _uiState.value.config
        viewModelScope.launch { manager.applyConfig(current, remember = false) }
    }

    // -------------------------------------------------------------------------
    // Salvar inventário
    // -------------------------------------------------------------------------

    fun saveInventory(inventoryName: String) {
        val tags = manager.tags.snapshot()
        if (tags.isEmpty() || inventoryName.isBlank()) return
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
                    prefixes = appSettings.prefixes,
                    inventoryName = inventoryName
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

    fun saveAppSettings(settings: AppSettings, onSaved: (() -> Unit)? = null) {
        viewModelScope.launch {
            val toSave = settings.copy(
                lastReaderId = reader?.readerId ?: _uiState.value.appSettings.lastReaderId
            )
            settingsRepo.save(toSave)
            _uiState.update { it.copy(appSettings = toSave) }
            onSaved?.invoke()
        }
    }
}
