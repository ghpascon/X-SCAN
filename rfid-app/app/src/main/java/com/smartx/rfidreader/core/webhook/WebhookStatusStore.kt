package com.smartx.rfidreader.core.webhook

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object WebhookStatusStore {
    private val _sending = MutableStateFlow(false)
    val sending: StateFlow<Boolean> = _sending.asStateFlow()

    private val _history = MutableStateFlow<List<WebhookSendStatus>>(emptyList())
    val history: StateFlow<List<WebhookSendStatus>> = _history.asStateFlow()

    fun setSending(value: Boolean) {
        _sending.value = value
    }

    fun add(status: WebhookSendStatus) {
        val max = 5
        val new = (_history.value + status).takeLast(max)
        _history.value = new
    }

    fun clear() {
        _history.value = emptyList()
    }
}
