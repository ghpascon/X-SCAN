package com.example.x_scan.rfid

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.KeyEvent
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import java.util.concurrent.CopyOnWriteArraySet

class RfidBridge(
    messenger: BinaryMessenger,
    private val manager: UhfSdkManager,
) {
    private val mainHandler = Handler(Looper.getMainLooper())

    private val methodChannel = MethodChannel(messenger, CHANNEL_METHOD)
    private val tagsChannel = EventChannel(messenger, CHANNEL_TAGS)
    private val connectedChannel = EventChannel(messenger, CHANNEL_CONNECTED)
    private val triggerChannel = EventChannel(messenger, CHANNEL_TRIGGER)

    @Volatile
    private var tagsSink: EventChannel.EventSink? = null

    @Volatile
    private var connectedSink: EventChannel.EventSink? = null

    @Volatile
    private var triggerSink: EventChannel.EventSink? = null

    private val pressedKeys = CopyOnWriteArraySet<Int>()

    init {
        manager.setListener(
            object : UhfListener {
                override fun onRead(tagsJson: String) {
                    emitTags(tagsJson)
                }

                override fun onConnect(isConnected: Boolean, powerLevel: Int) {
                    emitConnected(isConnected)
                }
            },
        )

        methodChannel.setMethodCallHandler { call, result ->
            handleMethodCall(call, result)
        }

        tagsChannel.setStreamHandler(
            simpleStreamHandler(
                onListen = { sink -> tagsSink = sink },
                onCancel = { tagsSink = null },
            ),
        )

        connectedChannel.setStreamHandler(
            simpleStreamHandler(
                onListen = { sink -> connectedSink = sink },
                onCancel = { connectedSink = null },
            ),
        )

        triggerChannel.setStreamHandler(
            simpleStreamHandler(
                onListen = { sink -> triggerSink = sink },
                onCancel = {
                    triggerSink = null
                    pressedKeys.clear()
                },
            ),
        )
    }

    fun onHardwareKeyDown(keyCode: Int): Boolean {
        if (!isTriggerKey(keyCode) || triggerSink == null) {
            return false
        }
        if (pressedKeys.add(keyCode)) {
            emitTrigger(true)
        }
        return true
    }

    fun onHardwareKeyUp(keyCode: Int): Boolean {
        if (!isTriggerKey(keyCode) || triggerSink == null) {
            return false
        }

        pressedKeys.remove(keyCode)
        if (pressedKeys.isEmpty()) {
            emitTrigger(false)
        }
        return true
    }

    private fun emitTags(payload: String) {
        runOnMainThread { tagsSink?.success(payload) }
    }

    private fun emitConnected(isConnected: Boolean) {
        runOnMainThread { connectedSink?.success(isConnected) }
    }

    private fun emitTrigger(pressed: Boolean) {
        runOnMainThread { triggerSink?.success(pressed) }
    }

    private fun runOnMainThread(block: () -> Unit) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            block()
            return
        }
        mainHandler.post(block)
    }

    private fun handleMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "getPlatformVersion" -> result.success("Android ${Build.VERSION.RELEASE}")
            "connect" -> result.success(manager.connect())
            "isConnected" -> result.success(manager.isConnected())
            "getReaderConfig" -> result.success(manager.getReaderConfig())
            "applyReaderConfig" -> {
                val power = call.argument<Number>("power")?.toInt()
                result.success(manager.applyReaderConfig(power))
            }
            "startContinuous" -> result.success(manager.startContinuous())
            "stop" -> result.success(manager.stop())
            "clearData" -> result.success(manager.clearData())
            "close" -> result.success(manager.close())
            else -> result.notImplemented()
        }
    }

    private fun isTriggerKey(keyCode: Int): Boolean {
        return keyCode in TRIGGER_KEY_CODES
    }

    private fun simpleStreamHandler(
        onListen: (EventChannel.EventSink) -> Unit,
        onCancel: () -> Unit,
    ): EventChannel.StreamHandler {
        return object : EventChannel.StreamHandler {
            override fun onListen(arguments: Any?, events: EventChannel.EventSink) {
                onListen(events)
            }

            override fun onCancel(arguments: Any?) {
                onCancel()
            }
        }
    }

    companion object {
        private const val CHANNEL_METHOD = "x_scan/rfid"
        private const val CHANNEL_TAGS = "x_scan/rfid/tags"
        private const val CHANNEL_CONNECTED = "x_scan/rfid/connected"
        private const val CHANNEL_TRIGGER = "x_scan/rfid/trigger"

        // Covers common trigger scan key codes on C-series rugged devices.
        private val TRIGGER_KEY_CODES = setOf(
            131,
            132,
            133,
            134,
            139,
            280,
            293,
            294,
            KeyEvent.KEYCODE_F1,
            KeyEvent.KEYCODE_F2,
            KeyEvent.KEYCODE_F3,
            KeyEvent.KEYCODE_F4,
        )
    }
}
