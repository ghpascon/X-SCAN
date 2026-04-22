package com.smartx.rfidreader.ui.selection

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.smartx.rfidreader.core.reader.IRfidReader
import com.smartx.rfidreader.core.reader.ReaderConnectionState
import com.smartx.rfidreader.core.registry.ReaderRegistry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SelectionUiState(
    val readers: List<IRfidReader> = emptyList(),
    val isConnecting: Boolean = false,
    val connectedReader: IRfidReader? = null,
    val errorMessage: String? = null
)

class ReaderSelectionViewModel(app: Application) : AndroidViewModel(app) {

    private val _uiState = MutableStateFlow(
        SelectionUiState(readers = ReaderRegistry.availableReaders)
    )
    val uiState: StateFlow<SelectionUiState> = _uiState.asStateFlow()

    fun connect(reader: IRfidReader) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isConnecting = true, errorMessage = null)
            val ok = reader.connect(getApplication())
            if (ok) {
                _uiState.value = _uiState.value.copy(
                    isConnecting = false,
                    connectedReader = reader
                )
            } else {
                _uiState.value = _uiState.value.copy(
                    isConnecting = false,
                    errorMessage = "Falha ao conectar ao leitor ${reader.displayName}.\nVerifique se o dispositivo está ligado."
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}
