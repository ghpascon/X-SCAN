package com.smartx.rfidreader.ui.main.radar

data class RadarTarget(
    val epc: String,
    val rssi: Double? = null,
    val lastSeenMs: Long = 0L,
    /** true se foi detectada na sessão de leitura atual (limpo ao iniciar nova leitura) */
    val detectedInSession: Boolean = false
) {
    /**
     * Badge "Detectada": persiste enquanto a sessão de leitura ativa não for reiniciada.
     * Não expira por tempo.
     */
    val isVisible: Boolean get() = detectedInSession

    /**
     * true se a tag foi lida nos últimos 2 segundos (para barra de sinal e buzzer).
     * O loop de radar zera lastSeenMs quando isso expira.
     */
    val isRecentlyRead: Boolean
        get() = lastSeenMs > 0L && System.currentTimeMillis() - lastSeenMs < 2000L

    /**
     * Força do sinal normalizada em 0.0..1.0 — baseada em leitura ativa (isRecentlyRead).
     * Faixa mapeada: -90 dBm → 0.0, -30 dBm → 1.0
     */
    val signalStrength: Float
        get() {
            if (lastSeenMs == 0L || rssi == null) return 0f
            val clamped = rssi.coerceIn(-90.0, -30.0)
            return ((clamped - (-90.0)) / ((-30.0) - (-90.0))).toFloat()
        }
}
