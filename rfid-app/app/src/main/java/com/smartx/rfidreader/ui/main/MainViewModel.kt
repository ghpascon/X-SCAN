package com.smartx.rfidreader.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.smartx.rfidreader.core.reader.IRfidReader
import com.smartx.rfidreader.core.reader.ReaderConfig
import com.smartx.rfidreader.core.reader.ReaderConnectionState
import com.smartx.rfidreader.core.reader.RfidTag
import com.smartx.rfidreader.core.registry.ReaderRegistry
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class MainUiState(
    val connectionState: ReaderConnectionState = ReaderConnectionState.DISCONNECTED,
    val isInventorying: Boolean = false,
    val config: ReaderConfig = ReaderConfig(),
    val isSavingConfig: Boolean = false,
    val configSaveSuccess: Boolean? = null,
    val statusMessage: String = ""
)

class MainViewModel(app: Application) : AndroidViewModel(app) {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val _tags = MutableStateFlow<List<RfidTag>>(emptyList())
    val tags: StateFlow<List<RfidTag>> = _tags.asStateFlow()

    var reader: IRfidReader? = null
        private set

    fun init(readerId: String) {
        val r = ReaderRegistry.findById(readerId) ?: return
        reader = r

        viewModelScope.launch {
            r.connectionState.collect { state ->
                _uiState.update { it.copy(connectionState = state) }
            }
        }

        viewModelScope.launch {
            r.tagFlow.collect { tag ->
                val current = _tags.value.toMutableList()
                val existing = current.indexOfFirst { it.epc == tag.epc }
                if (existing >= 0) {
                    current[existing] = current[existing].copy(
                        readCount = current[existing].readCount + 1,
                        rssi = tag.rssi,
                        timestamp = tag.timestamp
                    )
                } else {
                    current.add(0, tag)
                }
                _tags.value = current
            }
        }

        loadConfig()
    }

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
            _uiState.update { it.copy(isSavingConfig = false, configSaveSuccess = ok) }
        }
    }

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
        _tags.value = emptyList()
    }

    /** Para o inventário (se ativo) e limpa a lista de tags. Chamado ao sair da aba de leitura. */
    fun stopInventoryAndClear() {
        val r = reader ?: return
        if (r.isInventorying()) {
            r.stopInventory()
            _uiState.update { it.copy(isInventorying = false) }
        }
        _tags.value = emptyList()
    }

    fun onTriggerPressed() {
        val r = reader ?: return
        val pressed = r.onTriggerPressed()
        if (pressed) {
            _uiState.update { it.copy(isInventorying = r.isInventorying()) }
        }
    }

    fun onTriggerReleased() {
        reader?.onTriggerReleased()
        _uiState.update { it.copy(isInventorying = reader?.isInventorying() ?: false) }
    }

    fun consumeConfigSaveResult() {
        _uiState.update { it.copy(configSaveSuccess = null) }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.launch {
            reader?.disconnect()
        }
    }
}
