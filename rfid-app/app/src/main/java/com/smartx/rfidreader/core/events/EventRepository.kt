package com.smartx.rfidreader.core.events

import android.util.Log
import com.smartx.rfidreader.core.db.EventDao
import com.smartx.rfidreader.core.db.EventEntity
import com.smartx.rfidreader.core.reader.RfidTag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.roundToInt
import java.util.concurrent.TimeUnit

class EventRepository(private val dao: EventDao) {

    private val TAG = "EventRepository"

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    // Formato ISO-8601 com offset do fuso local (ex: 2024-04-22T14:30:00.000-03:00)
    private val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.US).apply {
        timeZone = TimeZone.getDefault()
    }

    val allEventsFlow: Flow<List<EventEntity>> = dao.allFlow()
    val pendingCountFlow: Flow<Int> = dao.pendingCountFlow()
    val totalCountFlow: Flow<Int> = dao.totalCountFlow()

    suspend fun saveInventory(
        deviceId: String,
        tags: List<RfidTag>,
        gpsLat: Double = 0.0,
        gpsLng: Double = 0.0,
        hasGps: Boolean = false,
        txPower: Int = 30,
        session: Int = 1,
        rssiFilter: Int = -120,
        prefixes: List<String> = emptyList(),
        inventoryName: String
    ): Long = withContext(Dispatchers.IO) {
        val tagsJson = buildTagsJson(tags)
        val timestamp = isoFormat.format(Date())
        val entity = EventEntity(
            deviceId = deviceId,
            inventoryName = inventoryName,
            tagsJson = tagsJson,
            savedAt = timestamp,
            gpsLat = gpsLat,
            gpsLng = gpsLng,
            hasGps = hasGps,
            txPower = txPower,
            session = session,
            rssiFilter = rssiFilter,
            prefixesJson = prefixes.joinToString("|")
        )
        dao.insert(entity)
    }

    suspend fun deleteEvent(event: EventEntity) = withContext(Dispatchers.IO) {
        dao.delete(event)
    }

    suspend fun deleteAllEvents() = withContext(Dispatchers.IO) {
        dao.deleteAll()
    }

    /**
     * Envia todos os eventos pendentes ao webhook.
     * Retorna Pair(successCount, failCount).
     */
    suspend fun sendPending(webhookUrl: String): Pair<Int, Int> = withContext(Dispatchers.IO) {
        val pending = dao.pending()
        var success = 0
        var fail = 0

        for (event in pending) {
            val ok = postEvent(event, webhookUrl)
            if (ok) {
                dao.update(event.copy(isSynced = true, syncedAt = isoFormat.format(Date())))
                success++
            } else {
                fail++
            }
        }
        Pair(success, fail)
    }

    /**
     * Envia pendentes com callback de progresso por evento.
     * Apaga do DB em caso de sucesso; mantém em caso de falha.
     */
    suspend fun sendPendingWithProgress(
        webhookUrl: String,
        onProgress: suspend (current: Int, total: Int, event: EventEntity, success: Boolean, error: String?) -> Unit
    ): Pair<Int, Int> {
        val pending = withContext(Dispatchers.IO) { dao.pending() }
        val total = pending.size
        var successCount = 0
        var failCount = 0
        pending.forEachIndexed { index, event ->
            val (ok, errMsg) = withContext(Dispatchers.IO) { postEventSafe(event, webhookUrl) }
            if (ok) {
                withContext(Dispatchers.IO) { dao.delete(event) }
                successCount++
            } else {
                failCount++
            }
            onProgress(index + 1, total, event, ok, errMsg)
        }
        return Pair(successCount, failCount)
    }

    private fun postEventSafe(event: EventEntity, url: String): Pair<Boolean, String?> {
        return try {
            val body = event.toWebhookJson().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url(url)
                .post(body)
                .header("Content-Type", "application/json")
                .build()
            httpClient.newCall(request).execute().use { response ->
                if (response.isSuccessful) Pair(true, null)
                else Pair(false, "HTTP ${response.code}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Falha ao enviar evento ${event.id}", e)
            Pair(false, e.message ?: "Erro de conexão")
        }
    }

    private fun postEvent(event: EventEntity, url: String): Boolean {
        return try {
            val body = event.toWebhookJson().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url(url)
                .post(body)
                .header("Content-Type", "application/json")
                .build()
            httpClient.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    Log.w(TAG, "Webhook retornou ${response.code} para evento ${event.id}")
                }
                response.isSuccessful
            }
        } catch (e: Exception) {
            Log.e(TAG, "Falha ao enviar evento ${event.id}", e)
            false
        }
    }

    private fun buildTagsJson(tags: List<RfidTag>): String {
        val arr = JSONArray()
        tags.forEach { tag ->
            // Sanitiza possíveis sufixos (" dBm"), vírgulas decimais e outros caracteres
            val rawRssi = tag.rssi
            val cleaned = rawRssi
                .replace(',', '.')
                .replace(Regex("[^0-9.-]"), "")
            val rssiInt = cleaned.toFloatOrNull()?.roundToInt()
                ?: tag.rssi.toIntOrNull()
                ?: 0

            arr.put(JSONObject().apply {
                put("epc", tag.epc)
                if (tag.tid.isNotEmpty()) put("tid", tag.tid)
                put("rssi", rssiInt)
                put("read_count", tag.readCount)
            })
        }
        return arr.toString()
    }
}
