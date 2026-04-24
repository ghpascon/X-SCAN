package com.smartx.rfidreader.core.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rfid_events")
data class EventEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val deviceId: String,
    val eventType: String = "inventory",
    /** JSON string representando a lista de tags */
    val tagsJson: String,
    /** ISO 8601 — momento em que o inventário foi salvo */
    val savedAt: String,
    val gpsLat: Double = 0.0,
    val gpsLng: Double = 0.0,
    val hasGps: Boolean = false,
    // Configurações do leitor no momento da captura
    val txPower: Int = 30,
    val session: Int = 1,
    val rssiFilter: Int = -120,
    val prefixesJson: String = "",
    val isSynced: Boolean = false,
    val syncedAt: String = ""
) {
    val tagCount: Int
        get() = try {
            org.json.JSONArray(tagsJson).length()
        } catch (_: Exception) { 0 }

    /** Monta o JSON completo pronto para envio ao webhook */
    fun toWebhookJson(): String {
        val gpsNode = if (hasGps) {
            org.json.JSONObject().apply {
                put("lat", gpsLat)
                put("lng", gpsLng)
            }
        } else org.json.JSONObject.NULL

        val prefixList = org.json.JSONArray().also { arr ->
            if (prefixesJson.isNotBlank()) {
                prefixesJson.split("|").filter { it.isNotBlank() }.forEach { arr.put(it) }
            }
        }

        val readerConfig = org.json.JSONObject().apply {
            put("tx_power", txPower)
            put("session", session)
            put("rssi_filter", rssiFilter)
            put("prefixes", prefixList)
        }

        val eventData = org.json.JSONObject().apply {
            put("tags", org.json.JSONArray(tagsJson))
            put("timestamp", savedAt)
            put("gps", gpsNode)
            put("reader_config", readerConfig)
        }
        return org.json.JSONObject().apply {
            put("device_name", deviceId)
            put("event_type", eventType)
            put("event_data", eventData)
        }.toString()
    }
}
