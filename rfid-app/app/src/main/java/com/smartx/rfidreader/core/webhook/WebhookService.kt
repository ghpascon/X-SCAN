package com.smartx.rfidreader.core.webhook

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.util.Log
import androidx.core.app.NotificationCompat
import com.smartx.rfidreader.core.reader.ReaderConnectionState
import com.smartx.rfidreader.core.registry.ReaderRegistry
import com.smartx.rfidreader.core.reader.RfidTag
import com.smartx.rfidreader.core.settings.AppSettings
import com.smartx.rfidreader.core.settings.AppSettingsRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
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
import java.util.concurrent.TimeUnit

class WebhookService : Service() {

    companion object {
        private const val TAG = "WebhookService"
        const val ACTION_START = "com.smartx.rfidreader.action.START_WEBHOOK"
        const val ACTION_STOP = "com.smartx.rfidreader.action.STOP_WEBHOOK"
        private const val CHANNEL_ID = "webhook_channel"
        private const val NOTIF_ID = 0xABCD
        @Volatile
        var isRunning = false
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val httpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .callTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    private val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.US).apply {
        timeZone = TimeZone.getDefault()
    }

    private val deviceId: String by lazy {
        Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID) ?: "unknown"
    }

    private val tagsMap = LinkedHashMap<String, RfidTag>()
    private val startedByService = mutableSetOf<String>()
    private var tagJob: Job? = null
    private var tickerJob: Job? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
        super.onCreate()
        createChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        when (action) {
            ACTION_START -> startForegroundServiceWork()
            ACTION_STOP -> stopForegroundServiceWork()
            else -> { /* ignore */ }
        }
        return START_STICKY
    }

    private fun startForegroundServiceWork() {
        if (isRunning) return
        isRunning = true

        val notif = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("X-SCAN: Webhook ativo")
            .setContentText("Aguardando envio periódico")
            .setSmallIcon(android.R.drawable.stat_notify_sync)
            .setOngoing(true)
            .build()

        startForeground(NOTIF_ID, notif)

        // Observa tags de todos os readers. Assim, se o leitor conectar depois do start,
        // o serviço continua recebendo tags sem precisar reiniciar.
        tagJob = scope.launch {
            val readers = ReaderRegistry.availableReaders
            if (readers.isEmpty()) {
                Log.w(TAG, "Nenhum reader registrado no ReaderRegistry")
                return@launch
            }

            readers.forEach { reader ->
                launch {
                    try {
                        reader.tagFlow.collect { tag -> upsertTag(tag) }
                    } catch (e: Exception) {
                        Log.e(TAG, "Collector error reader=${reader.readerId}", e)
                    }
                }
            }
        }

        // Ticker job to send posts periodically
        tickerJob = scope.launch {
            val settingsRepo = AppSettingsRepository(applicationContext)
            while (isActive) {
                val settings = try {
                    settingsRepo.flow.first()
                } catch (e: Exception) {
                    Log.e(TAG, "Falha ao ler AppSettings", e)
                    null
                }

                if (settings != null) {
                    ensureInventoryRunningForConnectedReaders()

                    val snapshot = snapshotTags()
                    WebhookStatusStore.setSending(true)
                    try {
                        val (ok, err) = sendPost(settings.webhookUrl, snapshot, settings)
                        if (ok && snapshot.isNotEmpty()) {
                            clearCollectedTags()
                        }
                        WebhookStatusStore.add(WebhookSendStatus(Date(), ok, snapshot.size, err))
                    } catch (e: Exception) {
                        Log.e(TAG, "Erro ao enviar webhook", e)
                        WebhookStatusStore.add(WebhookSendStatus(Date(), false, snapshot.size, e.message ?: "Erro de rede"))
                    } finally {
                        WebhookStatusStore.setSending(false)
                    }

                    delay(settings.webhookIntervalSeconds.coerceAtLeast(1) * 1000L)
                } else {
                    delay(1000L)
                }
            }
        }
    }

    private fun sendPost(url: String, tags: List<RfidTag>, settings: AppSettings): Pair<Boolean, String?> {
        if (url.isBlank()) {
            Log.d(TAG, "Webhook URL vazia — pular envio")
            return Pair(false, "URL vazia")
        }

        val body = buildWebhookPayload(tags, settings).toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url(url)
            .post(body)
            .header("Content-Type", "application/json")
            .build()

        return try {
            httpClient.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    Log.i(TAG, "Webhook enviado — tags=${tags.size} url=$url")
                    Pair(true, null)
                } else {
                    val msg = response.body?.string()?.take(2000) ?: response.message
                    Log.w(TAG, "Webhook falhou code=${response.code} msg=${msg}")
                    Pair(false, "HTTP ${response.code}: ${msg}")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Falha ao postar webhook", e)
            Pair(false, e.message ?: "Erro de rede")
        }
    }

    private fun buildWebhookPayload(tags: List<RfidTag>, settings: AppSettings): String {
        val arr = JSONArray()
        tags.forEach { tag ->
            val obj = JSONObject()
            obj.put("epc", tag.epc)
            if (tag.tid.isNotBlank()) obj.put("tid", tag.tid)
            val rssiInt = tag.rssi
                .replace(',', '.')
                .replace(Regex("[^0-9.-]"), "")
                .toFloatOrNull()
                ?.toInt()
                ?: 0
            obj.put("rssi", rssiInt)
            obj.put("antenna", tag.antenna)
            obj.put("read_count", tag.readCount)
            obj.put("timestamp", isoFormat.format(tag.timestamp))
            arr.put(obj)
        }

        val eventData = JSONObject().apply {
            put("timestamp", isoFormat.format(Date()))
            put("tags", arr)
            put("reader_config", JSONObject().apply {
                put("rssi_filter", settings.rssiFilter)
                put("prefixes", JSONArray(settings.prefixes))
            })
        }

        return JSONObject().apply {
            put("device_name", deviceId)
            put("event_type", "inventory_realtime")
            put("event_data", eventData)
        }.toString()
    }

    private fun snapshotTags(): List<RfidTag> = synchronized(tagsMap) {
        tagsMap.values.toList()
    }

    private fun clearCollectedTags() {
        synchronized(tagsMap) {
            tagsMap.clear()
        }
    }

    private fun ensureInventoryRunningForConnectedReaders() {
        ReaderRegistry.availableReaders.forEach { reader ->
            if (reader.connectionState.value != ReaderConnectionState.CONNECTED) return@forEach
            if (reader.isInventorying()) return@forEach

            val started = runCatching { reader.startInventory() }.getOrElse {
                Log.e(TAG, "Falha ao iniciar inventário em background (${reader.readerId})", it)
                false
            }

            if (started) {
                synchronized(startedByService) {
                    startedByService.add(reader.readerId)
                }
                Log.i(TAG, "Inventário iniciado pelo WebhookService (${reader.readerId})")
            }
        }
    }

    private fun stopInventoriesStartedByService() {
        val startedIds = synchronized(startedByService) { startedByService.toSet() }
        if (startedIds.isEmpty()) return

        ReaderRegistry.availableReaders.forEach { reader ->
            if (!startedIds.contains(reader.readerId)) return@forEach
            runCatching {
                if (reader.isInventorying()) {
                    reader.stopInventory()
                }
            }.onFailure {
                Log.e(TAG, "Falha ao parar inventário iniciado pelo serviço (${reader.readerId})", it)
            }
        }

        synchronized(startedByService) {
            startedByService.clear()
        }
    }

    private fun upsertTag(tag: RfidTag) {
        synchronized(tagsMap) {
            val existing = tagsMap[tag.epc]
            if (existing == null) {
                tagsMap[tag.epc] = tag
            } else {
                tagsMap[tag.epc] = existing.copy(
                    readCount = existing.readCount + 1,
                    rssi = tag.rssi,
                    timestamp = tag.timestamp
                )
            }
        }
    }

    private fun stopForegroundServiceWork() {
        if (!isRunning) return
        isRunning = false
        tagJob?.cancel()
        tickerJob?.cancel()
        stopInventoriesStartedByService()
        scope.coroutineContext.cancelChildren()
        stopForeground(true)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForegroundServiceWork()
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(CHANNEL_ID, "Webhook", NotificationManager.IMPORTANCE_LOW)
            nm.createNotificationChannel(channel)
        }
    }

}
