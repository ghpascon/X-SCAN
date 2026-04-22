package com.smartx.rfidreader.readers.ih25

import android.content.Context
import android.util.Log
import com.honeywell.rfidservice.EventListener
import com.honeywell.rfidservice.RfidManager
import com.honeywell.rfidservice.TriggerMode
import com.honeywell.rfidservice.rfid.AntennaPower
import com.honeywell.rfidservice.rfid.Gen2
import com.honeywell.rfidservice.rfid.OnTagReadListener
import com.honeywell.rfidservice.rfid.RfidReader
import com.honeywell.rfidservice.rfid.TagAdditionData
import com.honeywell.rfidservice.rfid.TagReadData
import com.honeywell.rfidservice.rfid.TagReadOption
import com.smartx.rfidreader.core.reader.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

/**
 * Adaptador para o leitor Honeywell IH25.
 * SDK: com.honeywell.rfidservice (RfidManager + RfidReader)
 *
 * Fluxo:
 *  1. connect() → manager.connect(null) → onDeviceConnected → manager.createReader()
 *  2. onReaderCreated(success, reader) → reader pronto, setOnTagReadListener configurado
 *  3. onRfidTriggered(pressed) → dispara startInventory / stopInventory automático
 *  4. disconnect() → reader.release() + manager.disconnect()
 */
class IH25Reader : IRfidReader {

    override val readerId: String = "IH25"
    override val displayName: String = "Honeywell IH25"
    override val isBle: Boolean = true

    private val TAG = "IH25Reader"

    /** Endereço MAC BLE do dispositivo selecionado pelo usuário */
    var targetMacAddress: String? = null

    private val _connectionState = MutableStateFlow(ReaderConnectionState.DISCONNECTED)
    override val connectionState: StateFlow<ReaderConnectionState> = _connectionState.asStateFlow()

    private var rfidManager: RfidManager? = null
    private var rfidReader: RfidReader? = null
    private var _isInventorying = false

    private val _tagChannel = MutableSharedFlow<RfidTag>(extraBufferCapacity = 64)
    override val tagFlow: Flow<RfidTag> = _tagChannel.asSharedFlow()

    private val tagReadListener = OnTagReadListener { data: Array<TagReadData> ->
        data.forEach { tagData ->
            val tidBytes = tagData.getAdditionData()
            val tid = if (tidBytes != null && tidBytes.isNotEmpty()) {
                tidBytes.joinToString("") { "%02X".format(it) }
            } else ""
            val tag = RfidTag(
                epc = tagData.getEpcHexStr() ?: "",
                rssi = tagData.getRssi().toString(),
                tid = tid
            )
            _tagChannel.tryEmit(tag)
        }
    }

    private val eventListener = object : EventListener {
        override fun onDeviceConnected(device: Any) {
            Log.i(TAG, "IH25 dispositivo conectado")
            _connectionState.value = ReaderConnectionState.CONNECTING
            // Dispositivo conectado — solicita criação do leitor RFID
            rfidManager?.createReader()
        }

        override fun onDeviceDisconnected(device: Any) {
            Log.i(TAG, "IH25 dispositivo desconectado")
            rfidReader = null
            _isInventorying = false
            _connectionState.value = ReaderConnectionState.DISCONNECTED
        }

        override fun onReaderCreated(success: Boolean, reader: RfidReader) {
            if (success) {
                Log.i(TAG, "IH25 reader criado com sucesso")
                rfidReader = reader
                reader.setOnTagReadListener(tagReadListener)
                rfidManager?.setTriggerMode(TriggerMode.RFID)
                _connectionState.value = ReaderConnectionState.CONNECTED
            } else {
                Log.e(TAG, "IH25 falha ao criar reader")
                _connectionState.value = ReaderConnectionState.ERROR
            }
        }

        override fun onRfidTriggered(pressed: Boolean) {
            if (pressed) onTriggerPressed() else onTriggerReleased()
        }

        override fun onTriggerModeSwitched(mode: TriggerMode) {
            Log.d(TAG, "Modo de gatilho alterado: $mode")
        }
    }

    override suspend fun connect(context: Context): Boolean = withContext(Dispatchers.IO) {
        _connectionState.value = ReaderConnectionState.CONNECTING
        return@withContext try {
            val manager = RfidManager.getInstance(context)
            manager.addEventListener(eventListener)
            val ok = manager.connect(targetMacAddress)
            rfidManager = manager
            if (!ok) {
                _connectionState.value = ReaderConnectionState.ERROR
            }
            ok
        } catch (e: Exception) {
            Log.e(TAG, "Falha ao conectar IH25", e)
            _connectionState.value = ReaderConnectionState.ERROR
            false
        }
    }

    override suspend fun disconnect() = withContext(Dispatchers.IO) {
        try {
            if (_isInventorying) stopInventory()
            rfidReader?.release()
            rfidManager?.removeEventListener(eventListener)
            rfidManager?.disconnect()
            rfidManager = null
            rfidReader = null
            _connectionState.value = ReaderConnectionState.DISCONNECTED
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao desconectar IH25", e)
        }
        Unit
    }

    override fun startInventory(): Boolean {
        val reader = rfidReader ?: return false
        val option = TagReadOption().apply { setData(true) }
        val ok = reader.read(TagAdditionData.TID_BANK, option)
        if (ok) _isInventorying = true
        return ok
    }

    override fun stopInventory(): Boolean {
        val reader = rfidReader ?: return false
        val ok = reader.stopRead()
        if (ok) _isInventorying = false
        return ok
    }

    override fun isInventorying() = _isInventorying

    override suspend fun applyConfig(config: ReaderConfig): Boolean = withContext(Dispatchers.IO) {
        val reader = rfidReader ?: return@withContext false
        var success = true
        try {
            // AntennaPower(id, readPower, writePower)
            val ap = AntennaPower(1, config.txPower, config.txPower)
            reader.setAntennaPower(arrayOf(ap))

            val session = when (config.session) {
                0 -> Gen2.Session.Session0
                1 -> Gen2.Session.Session1
                2 -> Gen2.Session.Session2
                else -> Gen2.Session.Session3
            }
            reader.setSession(session)
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao aplicar configuração IH25", e)
            success = false
        }
        success
    }

    override suspend fun readConfig(): ReaderConfig = withContext(Dispatchers.IO) {
        val reader = rfidReader ?: return@withContext ReaderConfig()
        return@withContext try {
            val powers = reader.getAntennaPower()
            val power = powers?.firstOrNull()?.readPower ?: 30
            val sessionEnum = reader.getSession()
            val sessionInt = when (sessionEnum) {
                Gen2.Session.Session0 -> 0
                Gen2.Session.Session1 -> 1
                Gen2.Session.Session2 -> 2
                Gen2.Session.Session3 -> 3
                else -> 1
            }
            ReaderConfig(txPower = power, session = sessionInt)
        } catch (e: Exception) {
            Log.e(TAG, "Erro ao ler configuração IH25", e)
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
