package com.smartx.rfidreader.core.webhook

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.smartx.rfidreader.core.registry.ReaderRegistry
import com.smartx.rfidreader.core.reader.ReaderConnectionState
import com.smartx.rfidreader.core.reader.RfidTag
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

    private val tagsMap = LinkedHashMap<String, RfidTag>()
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

        // Observe reader tags
        tagJob = scope.launch {
            val reader = ReaderRegistry.availableReaders.firstOrNull {
                it.connectionState.value == ReaderConnectionState.CONNECTED
            }
            if (reader != null) {
                try {
                    reader.tagFlow.collect { tag ->
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
                } catch (e: Exception) {
                    Log.e(TAG, "Collector error", e)
                }
            } else {
                // No reader connected — nothing to collect, will send empty lists periodically
                Log.i(TAG, "Nenhum leitor conectado — enviando listas vazias")
            }
        }

        // Ticker job to send posts periodically
        tickerJob = scope.launch {
            val settingsRepo = AppSettingsRepository(applicationContext)
            while (isActive) {
                val settings = try { settingsRepo.flow.first() } catch (e: Exception) { null }
                val interval = (settings?.webhookIntervalSeconds ?: 30).coerceAtLeast(1)
                delay(interval * 1000L)

                val snapshot: List<RfidTag> = synchronized(tagsMap) {
                    val s = tagsMap.values.toList()
                    tagsMap.clear()
                    s
                }

                val url = settings?.webhookUrl ?: ""
                // Update sending flag
                WebhookStatusStore.setSending(true)
                try {
                    val (ok, err) = sendPost(url, snapshot)
                    WebhookStatusStore.add(com.smartx.rfidreader.core.webhook.WebhookSendStatus(java.util.Date(), ok, snapshot.size, err))
                } catch (e: Exception) {
                    Log.e(TAG, "Erro ao enviar webhook", e)
                    WebhookStatusStore.add(com.smartx.rfidreader.core.webhook.WebhookSendStatus(java.util.Date(), false, snapshot.size, e.message ?: "Erro de rede"))
                } finally {
                    WebhookStatusStore.setSending(false)
                }
            }
        }
    }

    private fun sendPost(url: String, tags: List<RfidTag>): Pair<Boolean, String?> {
        if (url.isBlank()) {
            Log.d(TAG, "Webhook URL vazia — pular envio")
            return Pair(false, "URL vazia")
        }

        val arr = JSONArray()
        tags.forEach { tag ->
            val obj = JSONObject()
            obj.put("epc", tag.epc)
            if (tag.tid.isNotBlank()) obj.put("tid", tag.tid)
            val rssiInt = tag.rssi.replace(',', '.').replace(Regex("[^0-9.-]"), "").toFloatOrNull()?.toInt() ?: 0
            obj.put("rssi", rssiInt)
            obj.put("antenna", tag.antenna)
            obj.put("read_count", tag.readCount)
            obj.put("timestamp", isoFormat.format(tag.timestamp))
            arr.put(obj)
        }

        val body = arr.toString().toRequestBody("application/json".toMediaType())
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

    private fun stopForegroundServiceWork() {
        if (!isRunning) return
        isRunning = false
        tagJob?.cancel()
        tickerJob?.cancel()
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
