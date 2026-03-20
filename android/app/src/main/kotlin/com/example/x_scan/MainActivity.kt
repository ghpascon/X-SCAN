package com.example.x_scan
import android.util.Log

import android.content.pm.PackageManager
import android.view.KeyEvent
import com.example.x_scan.rfid.RfidBridge
import com.example.x_scan.rfid.UhfSdkManager
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {
	private val channelName = "x_scan/hardware_features"
	private var rfidBridge: RfidBridge? = null

	override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
		super.configureFlutterEngine(flutterEngine)

		rfidBridge = RfidBridge(
			messenger = flutterEngine.dartExecutor.binaryMessenger,
			manager = UhfSdkManager(applicationContext),
		)

		MethodChannel(flutterEngine.dartExecutor.binaryMessenger, channelName)
			.setMethodCallHandler { call, result ->
				if (call.method == "getHardwareFeatures") {
					result.success(getHardwareFeatures())
				} else {
					result.notImplemented()
				}
			}
	}

	private fun getHardwareFeatures(): Map<String, Boolean> {
		val pm = packageManager

		return mapOf(
			"rfidNfc" to pm.hasSystemFeature(PackageManager.FEATURE_NFC),
			"bluetooth" to pm.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH),
			"bluetoothLe" to pm.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE),
			"camera" to pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY),
			"cameraAutofocus" to pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS),
			"barcodeScanner" to (
				pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY) &&
					pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS)
				),
			"fingerprint" to pm.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT),
			"wifi" to pm.hasSystemFeature(PackageManager.FEATURE_WIFI),
			"usbHost" to pm.hasSystemFeature(PackageManager.FEATURE_USB_HOST),
			"microphone" to pm.hasSystemFeature(PackageManager.FEATURE_MICROPHONE),
			"gps" to pm.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)
		)
	}

	override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
		Log.d("XSCAN_KEYS", "onKeyDown: keyCode=$keyCode, event=$event")
		if (rfidBridge?.onHardwareKeyDown(keyCode) == true) {
			return true
		}
		return super.onKeyDown(keyCode, event)
	}

	override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
		Log.d("XSCAN_KEYS", "onKeyUp: keyCode=$keyCode, event=$event")
		if (rfidBridge?.onHardwareKeyUp(keyCode) == true) {
			return true
		}
		return super.onKeyUp(keyCode, event)
	}
}
