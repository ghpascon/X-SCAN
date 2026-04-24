package com.smartx.rfidreader.readers.at907

import android.content.Context
import android.util.Log
import com.atid.lib.dev.ATRfidManager
import com.atid.lib.dev.ATRfidReader
import com.atid.lib.dev.event.RfidReaderEventListener
import com.atid.lib.dev.rfid.exception.ATRfidReaderException
import com.atid.lib.dev.rfid.type.ActionState
import com.atid.lib.dev.rfid.type.ConnectionState
import java.util.Locale
import com.atid.lib.dev.rfid.type.GlobalBandType
import com.atid.lib.dev.rfid.type.InventorySession
import com.atid.lib.dev.rfid.type.ResultCode
import com.smartx.rfidreader.core.reader.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

/**
 * Adaptador para o leitor Chainway AT907.
 * SDK: com.atid.lib.dev (ATRfidManager / ATRfidReader)
 *
 * IMPORTANTE: ATRfidManager.getInstance() cria Handlers internamente — todas as chamadas
 * ao SDK devem ocorrer na Main (Looper) thread, jamais em Dispatchers.IO.
 *
 * Escala de potência: SDK usa décimos de dBm → setPower(300) = 30.0 dBm
 * Região: GlobalBandType.Brazil disponível
 * Modo de inventário: SDK não expõe EPC+TID no callback — sempre retorna só EPC+RSSI
 */
class AT907Reader : IRfidReader {

    override val readerId: String = "AT907"
    override val displayName: String = "Chainway AT907"

    private val TAG = "AT907Reader"

    // Limites de potência em unidades do SDK (décimos de dBm)
    private var powerMin = 50   // 5.0 dBm
    private var powerMax = 330  // 33.0 dBm

    private val _connectionState = MutableStateFlow(ReaderConnectionState.DISCONNECTED)
    override val connectionState: StateFlow<ReaderConnectionState> = _connectionState.asStateFlow()

    private var reader: ATRfidReader? = null
    private var _isInventorying = false

    private val _tagChannel = MutableSharedFlow<RfidTag>(extraBufferCapacity = 128)
    override val tagFlow: Flow<RfidTag> = _tagChannel.asSharedFlow()

    private val eventListener = object : RfidReaderEventListener {
        override fun onReaderStateChanged(r: ATRfidReader, state: ConnectionState) {
            Log.d(TAG, "onReaderStateChanged: $state")
            _connectionState.value = when (state) {
                ConnectionState.Connected -> ReaderConnectionState.CONNECTED
                ConnectionState.Connecting -> ReaderConnectionState.CONNECTING
                ConnectionState.Disconnected -> {
                    _isInventorying = false
                    ReaderConnectionState.DISCONNECTED
                }
                else -> ReaderConnectionState.DISCONNECTED
            }
        }

        override fun onReaderActionChanged(r: ATRfidReader, action: ActionState) {
            Log.d(TAG, "onReaderActionChanged: $action")
            _isInventorying = when (action) {
                ActionState.Inventory6cMulti,
                ActionState.Inventory6cSingle,
                ActionState.InventoryAnyMulti -> true
                else -> false
            }
        }

        override fun onReaderReadTag(r: ATRfidReader, epc: String, rssi: Float, f: Float) {
            if (epc.isNotBlank()) {
                // SDK prepends 2-byte PC word (e.g. "3400") to the EPC — strip the first 4 hex chars
                val cleanEpc = if (epc.length > 4) epc.substring(4) else epc
                _tagChannel.tryEmit(
                    RfidTag(
                        epc = cleanEpc,
                        rssi = "%.1f".format(Locale.US, rssi)
                    )
                )
            }
        }

        override fun onReaderResult(
            r: ATRfidReader,
            result: ResultCode,
            action: ActionState,
            s1: String,
            s2: String,
            f1: Float,
            f2: Float
        ) {
            if (result != ResultCode.NoError) {
                Log.w(TAG, "onReaderResult: action=$action result=$result s1=$s1")
            }
        }
    }

    override suspend fun connect(context: Context): Boolean = withContext(Dispatchers.Main) {
        _connectionState.value = ReaderConnectionState.CONNECTING
        return@withContext try {
            val r = ATRfidManager.getInstance()
            r.setEventListener(eventListener)

            // Lê range de potência do hardware
            try {
                val range = r.getPowerRange()
                powerMin = range.min
                powerMax = range.max
                Log.i(TAG, "Power range: $powerMin–$powerMax (décimos dBm)")
            } catch (e: ATRfidReaderException) {
                Log.w(TAG, "Não foi possível ler power range, usando defaults")
            }

            val ok = r.connect()
            if (ok) {
                reader = r
                Log.i(TAG, "Conectado ao AT907")
                true
            } else {
                _connectionState.value = ReaderConnectionState.ERROR
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Falha ao conectar AT907", e)
            _connectionState.value = ReaderConnectionState.ERROR
            false
        }
    }

    override suspend fun disconnect() = withContext(Dispatchers.Main) {
        try {
            if (_isInventorying) {
                reader?.stop()
                _isInventorying = false
            }
            reader?.disconnect()
            reader = null
            _connectionState.value = ReaderConnectionState.DISCONNECTED
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao desconectar AT907", e)
        }
        Unit
    }

    override fun startInventory(): Boolean {
        val r = reader ?: return false
        if (_isInventorying) return true  // já inventariando
        return try {
            val result = r.inventory6cTag()
            val ok = result == ResultCode.NoError
            if (ok) {
                _isInventorying = true
                Log.i(TAG, "Inventário iniciado")
            } else {
                Log.w(TAG, "inventory6cTag() retornou: $result")
            }
            ok
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao iniciar inventário", e)
            false
        }
    }

    override fun stopInventory(): Boolean {
        val r = reader ?: run {
            _isInventorying = false
            return false
        }
        return try {
            val result = r.stop()
            Log.i(TAG, "stop() retornou: $result")
            // O estado real de _isInventorying é atualizado via onReaderActionChanged;
            // mas forçamos false imediatamente para a UI responder rápido
            _isInventorying = false
            true
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao parar inventário", e)
            _isInventorying = false
            false
        }
    }

    override fun isInventorying() = _isInventorying

    override suspend fun applyConfig(config: ReaderConfig): Boolean = withContext(Dispatchers.Main) {
        val r = reader ?: return@withContext false
        var success = true
        try {
            // Potência: UI em dBm, SDK em décimos de dBm (30 dBm → 300)
            val sdkPower = (config.txPower * 10).coerceIn(powerMin, powerMax)
            r.setPower(sdkPower)
            Log.d(TAG, "setPower($sdkPower) = ${config.txPower} dBm")

            // Session Gen2
            val session = when (config.session) {
                0 -> InventorySession.S0
                1 -> InventorySession.S1
                2 -> InventorySession.S2
                else -> InventorySession.S3
            }
            r.setInventorySession(session)

            // Região: Brasil disponível
            r.setGlobalBand(GlobalBandType.Brazil)

        } catch (e: ATRfidReaderException) {
            Log.e(TAG, "Erro ao aplicar configuração AT907", e)
            success = false
        }
        success
    }

    override suspend fun readConfig(): ReaderConfig = withContext(Dispatchers.Main) {
        val r = reader ?: return@withContext ReaderConfig()
        return@withContext try {
            // SDK retorna décimos de dBm → dividir por 10 para exibir em dBm
            val sdkPower = r.getPower()
            val power = sdkPower / 10

            val sessionInt = when (r.getInventorySession()) {
                InventorySession.S0 -> 0
                InventorySession.S1 -> 1
                InventorySession.S2 -> 2
                else -> 3
            }
            Log.d(TAG, "readConfig: sdkPower=$sdkPower → $power dBm, session=$sessionInt")
            ReaderConfig(
                txPower = power,
                session = sessionInt,
                inventoryMode = ReaderConfig.InventoryMode.EPC_TID  // AT907 sempre lê EPC; TID é best-effort
            )
        } catch (e: ATRfidReaderException) {
            Log.e(TAG, "Erro ao ler configuração AT907", e)
            ReaderConfig()
        }
    }

    override fun onTriggerPressed(): Boolean {
        if (_connectionState.value != ReaderConnectionState.CONNECTED) return false
        if (!_isInventorying) startInventory()
        return true
    }

    override fun onTriggerReleased(): Boolean {
        if (_isInventorying) stopInventory()
        return true
    }
}

