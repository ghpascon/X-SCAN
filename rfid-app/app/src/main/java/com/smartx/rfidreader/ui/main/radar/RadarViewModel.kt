package com.smartx.rfidreader.ui.main.radar

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.smartx.rfidreader.core.reader.RfidTag
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RadarViewModel(app: Application) : AndroidViewModel(app) {

    private val _targets = MutableStateFlow<List<RadarTarget>>(emptyList())
    val targets: StateFlow<List<RadarTarget>> = _targets.asStateFlow()

    /**
     * Melhor RSSI de qualquer target lida nos últimos 500ms.
     * null = nenhuma tag ativa → buzzer deve parar.
     */
    private val _activeBestRssi = MutableStateFlow<Double?>(null)
    val activeBestRssi: StateFlow<Double?> = _activeBestRssi.asStateFlow()

    private val targetMap = LinkedHashMap<String, RadarTarget>()
    private var buzzerJob: Job? = null

    // -------------------------------------------------------------------------
    // Gerenciamento de targets
    // -------------------------------------------------------------------------

    /** Adiciona um EPC à lista de targets. Retorna false se já existir ou for inválido. */
    fun addTarget(epc: String): Boolean {
        val normalized = epc.trim().uppercase()
        if (normalized.isBlank() || targetMap.containsKey(normalized)) return false
        targetMap[normalized] = RadarTarget(epc = normalized)
        _targets.value = targetMap.values.toList()
        return true
    }

    /** Adiciona múltiplos EPCs (ignora duplicatas silenciosamente). */
    fun addTargets(epcs: List<String>) {
        epcs.forEach { epc ->
            val normalized = epc.trim().uppercase()
            if (normalized.isNotBlank() && !targetMap.containsKey(normalized)) {
                targetMap[normalized] = RadarTarget(epc = normalized)
            }
        }
        _targets.value = targetMap.values.toList()
    }

    /** Remove um EPC da lista de targets. */
    fun removeTarget(epc: String) {
        targetMap.remove(epc.uppercase())
        _targets.value = targetMap.values.toList()
    }

    /** Remove todos os targets. */
    fun clearTargets() {
        targetMap.clear()
        _targets.value = emptyList()
    }

    // -------------------------------------------------------------------------
    // Atualização de sinal (chamado pelo Fragment a partir do tagFlow)
    // -------------------------------------------------------------------------

    /**
     * Parseia o campo rssi (String) do hardware de forma robusta.
     * Lida com vírgula decimal (locale pt-BR), sufixo " dBm" e espaços.
     */
    private fun parseRssi(raw: String): Double? =
        raw.trim().replace(',', '.').removeSuffix("dBm").trim().toDoubleOrNull()

    /**
     * Recebe um evento individual do tagFlow (leitura fresca do hardware).
     * Atualiza RSSI, lastSeenMs e marca tag como detectada na sessão atual.
     */
    fun onSingleTagRead(tag: RfidTag) {
        val epc = tag.epc.uppercase()
        val current = targetMap[epc] ?: return
        val rssi = parseRssi(tag.rssi)
        targetMap[epc] = current.copy(
            rssi = rssi ?: current.rssi,
            lastSeenMs = System.currentTimeMillis(),
            detectedInSession = true
        )
        _targets.value = targetMap.values.toList()
    }

    /**
     * Limpa o estado de "detectada" de todos os targets.
     * Deve ser chamado quando uma nova sessão de leitura se inicia.
     */
    fun onScanStarted() {
        for ((epc, target) in targetMap) {
            targetMap[epc] = target.copy(detectedInSession = false, lastSeenMs = 0L)
        }
        _targets.value = targetMap.values.toList()
    }

    // -------------------------------------------------------------------------
    // Loop de buzzer por intervalo RSSI
    // -------------------------------------------------------------------------

    /**
     * Inicia o loop de buzzer radar. Deve ser chamado em onStart do Fragment.
     * O intervalo entre bipes diminui quanto mais forte for o sinal da melhor target visível.
     *
     * Mapeamento (linear):
     * RSSI ≥ -30 dBm → 120 ms entre bipes (muito próximo)
     * RSSI = -60 dBm → ~580 ms
     * RSSI ≤ -90 dBm → 2500 ms (muito longe — apenas confirma que está no campo)
     */
    fun startBuzzerLoop() {
        buzzerJob?.cancel()
        buzzerJob = viewModelScope.launch {
            while (true) {
                delay(100L) // tick rápido para RSSI responsivo

                val now = System.currentTimeMillis()

                // Expira barras de sinal na UI após 2s
                val keysSnapshot = targetMap.keys.toList()
                var anyExpired = false
                for (epc in keysSnapshot) {
                    val target = targetMap[epc] ?: continue
                    if (target.lastSeenMs > 0L && now - target.lastSeenMs >= 2000L) {
                        targetMap[epc] = target.copy(lastSeenMs = 0L)
                        anyExpired = true
                    }
                }
                if (anyExpired) _targets.value = targetMap.values.toList()

                // Melhor RSSI das tags lidas nos últimos 500ms (janela do buzzer)
                val bestRssi = targetMap.values
                    .filter { it.lastSeenMs > 0L && now - it.lastSeenMs < 500L }
                    .mapNotNull { it.rssi }
                    .maxOrNull()
                _activeBestRssi.value = bestRssi
            }
        }
    }

    /** Para o loop de buzzer radar. Deve ser chamado em onStop do Fragment. */
    fun stopBuzzerLoop() {
        buzzerJob?.cancel()
        buzzerJob = null
    }

    override fun onCleared() {
        super.onCleared()
        buzzerJob?.cancel()
    }
}
