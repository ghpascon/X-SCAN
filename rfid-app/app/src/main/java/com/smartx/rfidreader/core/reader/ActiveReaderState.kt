package com.smartx.rfidreader.core.reader

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Estado global do leitor ativo, visível por qualquer tela do app.
 *
 * O MainViewModel escreve aqui sempre que o estado muda.
 * O BaseActivity observa automaticamente — portanto qualquer Activity
 * (inclusive as que não têm acesso ao MainViewModel) exibe o estado real.
 */
object ActiveReaderState {

    private val _readerName = MutableStateFlow("Nenhum leitor")
    val readerName: StateFlow<String> = _readerName.asStateFlow()

    private val _connectionState = MutableStateFlow(ReaderConnectionState.DISCONNECTED)
    val connectionState: StateFlow<ReaderConnectionState> = _connectionState.asStateFlow()

    private val _isInventorying = MutableStateFlow(false)
    val isInventorying: StateFlow<Boolean> = _isInventorying.asStateFlow()

    fun update(
        readerName: String,
        connectionState: ReaderConnectionState,
        isInventorying: Boolean
    ) {
        _readerName.value = readerName
        _connectionState.value = connectionState
        _isInventorying.value = isInventorying
    }
}
