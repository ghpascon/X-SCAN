package com.smartx.rfidreader.ui.sync

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.smartx.rfidreader.RfidApplication
import com.smartx.rfidreader.core.db.EventEntity
import com.smartx.rfidreader.core.events.EventRepository
import com.smartx.rfidreader.core.settings.AppSettingsRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// ──────────────────────────────────────────────────────────────────────────────
// Estado de progresso por evento
// ──────────────────────────────────────────────────────────────────────────────

enum class ItemStatus { WAITING, SUCCESS, ERROR }

data class SyncProgressItem(
    val eventId: Long,
    val label: String,
    val status: ItemStatus = ItemStatus.WAITING,
    val errorMessage: String? = null
)

data class SyncProgressState(
    val isRunning: Boolean = false,
    val current: Int = 0,
    val total: Int = 0,
    val items: List<SyncProgressItem> = emptyList(),
    val finalMessage: String? = null   // null = não terminou ainda
)

// ──────────────────────────────────────────────────────────────────────────────
// UI state geral
// ──────────────────────────────────────────────────────────────────────────────

data class SyncUiState(
    val webhookUrl: String = ""
)

// ──────────────────────────────────────────────────────────────────────────────
// ViewModel
// ──────────────────────────────────────────────────────────────────────────────

class SyncViewModel(app: Application) : AndroidViewModel(app) {

    private val rfidApp = app as RfidApplication
    private val eventRepo: EventRepository = rfidApp.eventRepository
    private val settingsRepo: AppSettingsRepository = rfidApp.settingsRepository

    private val _uiState = MutableStateFlow(SyncUiState())
    val uiState: StateFlow<SyncUiState> = _uiState.asStateFlow()

    private val _syncProgress = MutableStateFlow(SyncProgressState())
    val syncProgress: StateFlow<SyncProgressState> = _syncProgress.asStateFlow()

    val events: StateFlow<List<EventEntity>> = eventRepo.allEventsFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    val pendingCount: StateFlow<Int> = eventRepo.pendingCountFlow
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    init {
        viewModelScope.launch {
            settingsRepo.flow.collect { settings ->
                _uiState.update { it.copy(webhookUrl = settings.webhookUrl) }
            }
        }
    }

    fun saveWebhookUrl(url: String) {
        viewModelScope.launch {
            val current = settingsRepo.flow.first()
            settingsRepo.save(current.copy(webhookUrl = url.trim()))
            _uiState.update { it.copy(webhookUrl = url.trim()) }
        }
    }

    /**
     * Inicia o envio com progresso em tempo real por evento.
     * [onNoUrl] é chamado na thread principal se a URL estiver vazia.
     */
    fun startSyncWithProgress(onNoUrl: () -> Unit) {
        if (_syncProgress.value.isRunning) return
        val url = _uiState.value.webhookUrl
        if (url.isBlank()) { onNoUrl(); return }

        viewModelScope.launch {
            _syncProgress.update { SyncProgressState(isRunning = true) }

            val (successCount, failCount) = eventRepo.sendPendingWithProgress(url) { current, total, event, success, error ->
                val label = event.shortLabel()
                _syncProgress.update { state ->
                    val updatedItems = state.items.toMutableList()

                    // Adiciona entradas "em espera" para itens ainda não visíveis
                    while (updatedItems.size < total) {
                        val idx = updatedItems.size
                        val pendingLabel = events.value.getOrNull(idx)?.shortLabel() ?: "Evento ${idx + 1}"
                        updatedItems.add(SyncProgressItem(eventId = -1L * (idx + 1), label = pendingLabel))
                    }

                    // Atualiza o item que acabou de ser processado
                    val itemIndex = current - 1
                    if (itemIndex in updatedItems.indices) {
                        updatedItems[itemIndex] = SyncProgressItem(
                            eventId = event.id,
                            label = label,
                            status = if (success) ItemStatus.SUCCESS else ItemStatus.ERROR,
                            errorMessage = if (!success) error else null
                        )
                    }

                    state.copy(current = current, total = total, items = updatedItems.toList())
                }
            }

            val msg = when {
                failCount == 0 && successCount > 0 -> "${successCount} enviado(s) com sucesso"
                failCount > 0 && successCount > 0 -> "${successCount} enviado(s), ${failCount} com erro"
                failCount > 0 && successCount == 0 -> "Todos os ${failCount} evento(s) falharam"
                else -> "Nenhum pendente"
            }
            _syncProgress.update { it.copy(isRunning = false, finalMessage = msg) }
        }
    }

    fun resetProgress() {
        _syncProgress.update { SyncProgressState() }
    }

    fun deleteEvent(event: EventEntity) {
        viewModelScope.launch { eventRepo.deleteEvent(event) }
    }

    fun deleteAllEvents() {
        viewModelScope.launch { eventRepo.deleteAllEvents() }
    }
}

private fun EventEntity.shortLabel(): String {
    // savedAt é ISO-8601 "2024-04-22T14:30:00.000Z"
    val datePart = runCatching {
        val dt = savedAt.substring(0, 16) // "2024-04-22T14:30"
        "${dt.substring(8, 10)}/${dt.substring(5, 7)} ${dt.substring(11, 16)}"
    }.getOrElse { savedAt }
    val tagCount = runCatching {
        org.json.JSONArray(tagsJson).length()
    }.getOrElse { 0 }
    return "$datePart · $tagCount tags"
}
