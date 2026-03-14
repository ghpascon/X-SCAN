package com.example.x_scan

import android.content.pm.PackageManager
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.android.FlutterActivity
import io.flutter.plugin.common.MethodChannel

class MainActivity : FlutterActivity() {
	private val channelName = "x_scan/hardware_features"

	override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
		super.configureFlutterEngine(flutterEngine)

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
}
