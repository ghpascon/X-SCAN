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
    private val oldTagKeys = mutableSetOf<String>()

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
            if (connected) {
                // Prefer inventory payload with EPC+TID when supported by firmware.
                try {
                    reader?.setEPCAndTIDMode()
                } catch (_: Throwable) {
                    // Keep connected even if this mode is unavailable in current firmware.
                }
            }
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

        // Mark all current tags as old so only new ones get logged
        oldTagKeys.clear()
        oldTagKeys.addAll(tagMap.keys)

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

    fun getReaderConfig(): Map<String, Any?> {
        val connected = connect()
        if (!connected) {
            return mapOf(
                "connected" to false,
                "power" to null,
            )
        }

        val currentReader = reader ?: return mapOf(
            "connected" to false,
            "power" to null,
        )

        val power = try {
            currentReader.power
        } catch (_: Throwable) {
            null
        }

        return mapOf(
            "connected" to true,
            "power" to power,
        )
    }

    fun applyReaderConfig(power: Int?): Boolean {
        if (!connect()) {
            return false
        }

        val currentReader = reader ?: return false

        if (power == null) {
            return true
        }

        return currentReader.setPower(power.coerceIn(5, 30))
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

        val tid = extractTid(info)
        val rssi = extractRssi(info)
        // Use TID as primary key if available, otherwise use EPC
        val key = if (tid.isNotBlank()) tid else epc
        val existing = tagMap[key]
        val nextCount = (existing?.count?.toIntOrNull() ?: 0) + 1

        // Only log new tags (not from old session)
        if (nextCount == 1) {
            android.util.Log.d("UhfSdkManager", "TAG: epc=$epc, tid=$tid, rssi=$rssi, count=$nextCount")
        }

        tagMap[key] = EpcTag(
            id = existing?.id ?: "",
            epc = epc,
            tid = if (tid.isNotBlank()) tid else (existing?.tid ?: ""),
            count = nextCount.toString(),
            rssi = if (rssi.isNotBlank()) rssi else (existing?.rssi ?: ""),
        )

        publishTags()
    }

    private fun publishTags() {
        val array = JSONArray()
        tagMap.values.forEach { tag ->
            val json = JSONObject()
            json.put(TagKey.ID, tag.id)
            json.put(TagKey.EPC, tag.epc)
            json.put(TagKey.TID, tag.tid)
            json.put(TagKey.RSSI, tag.rssi)
            json.put(TagKey.COUNT, tag.count)
            array.put(json)
        }
        listener?.onRead(array.toString())
    }

    private fun extractTid(info: UHFTAGInfo): String {
        return try {
            // Use the direct getter method from SDK: getTid() returns String
            val tid = info.getTid()?.trim()
            if (!tid.isNullOrBlank()) {
                tid
            } else {
                ""
            }
        } catch (_: Throwable) {
            ""
        }
    }

    private fun extractRssi(info: UHFTAGInfo): String {
        return try {
            // Use the direct getter method from SDK: getRssi() returns String
            val rssiStr = info.getRssi()?.trim() ?: ""
            if (rssiStr.isBlank()) {
                return ""
            }
            
            // Convert decimal string (e.g., "-72,50" or "-72.50") to integer
            // Replace comma with dot for parsing
            val normalized = rssiStr.replace(",", ".")
            val rssiDouble = normalized.toDoubleOrNull()
            
            if (rssiDouble != null) {
                rssiDouble.toInt().toString()
            } else {
                rssiStr  // Return original if parsing fails
            }
        } catch (_: Throwable) {
            ""
        }
    }
}
