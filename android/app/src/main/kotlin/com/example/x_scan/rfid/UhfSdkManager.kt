package com.example.x_scan.rfid

import android.content.Context
import com.rscja.deviceapi.RFIDWithUHFUART
import com.rscja.deviceapi.entity.UHFTAGInfo
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.ConcurrentHashMap
import kotlin.concurrent.thread

class UhfSdkManager(private val context: Context) {
    private var reader: RFIDWithUHFUART? = null
    private var listener: UhfListener? = null
    private val tagMap = ConcurrentHashMap<String, EpcTag>()

    @Volatile
    private var started = false

    fun setListener(listener: UhfListener?) {
        this.listener = listener
    }

    fun connect(): Boolean {
        return try {
            if (reader == null) {
                reader = RFIDWithUHFUART.getInstance()
            }
            val connected = reader?.init(context) == true
            listener?.onConnect(connected, 0)
            connected
        } catch (_: Throwable) {
            listener?.onConnect(false, 0)
            false
        }
    }

    fun isConnected(): Boolean {
        return reader != null
    }

    fun startContinuous(): Boolean {
        val currentReader = reader ?: return false
        if (started) {
            return true
        }

        val ok = currentReader.startInventoryTag()
        if (!ok) {
            return false
        }

        started = true
        startReadLoop()
        return true
    }

    fun stop(): Boolean {
        val currentReader = reader
        if (!started || currentReader == null) {
            started = false
            return false
        }

        started = false
        return currentReader.stopInventory()
    }

    fun clearData(): Boolean {
        tagMap.clear()
        return true
    }

    fun close(): Boolean {
        started = false
        reader?.free()
        reader = null
        listener?.onConnect(false, 0)
        clearData()
        return true
    }

    private fun startReadLoop() {
        thread(name = "rfid-read-loop", isDaemon = true) {
            while (started) {
                val currentReader = reader ?: break
                val tag = currentReader.readTagFromBuffer() ?: continue
                appendTag(tag)
            }
        }
    }

    private fun appendTag(info: UHFTAGInfo) {
        val epc = info.epc ?: ""
        if (epc.isBlank()) {
            return
        }

        val rssi = info.rssi ?: ""
        val existing = tagMap[epc]
        val nextCount = (existing?.count?.toIntOrNull() ?: 0) + 1

        tagMap[epc] = EpcTag(
            id = existing?.id ?: "",
            epc = epc,
            count = nextCount.toString(),
            rssi = rssi,
        )

        publishTags()
    }

    private fun publishTags() {
        val array = JSONArray()
        tagMap.values.forEach { tag ->
            val json = JSONObject()
            json.put(TagKey.ID, tag.id)
            json.put(TagKey.EPC, tag.epc)
            json.put(TagKey.RSSI, tag.rssi)
            json.put(TagKey.COUNT, tag.count)
            array.put(json)
        }
        listener?.onRead(array.toString())
    }
}
