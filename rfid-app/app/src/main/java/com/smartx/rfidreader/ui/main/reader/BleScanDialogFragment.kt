package com.smartx.rfidreader.ui.main.reader

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartx.rfidreader.R
import com.smartx.rfidreader.databinding.DialogBleScanBinding

/**
 * Diálogo que escaneia dispositivos BLE e permite ao usuário selecionar um.
 * Retorna o endereço MAC via [onDeviceSelected].
 */
class BleScanDialogFragment : DialogFragment() {

    private var _binding: DialogBleScanBinding? = null
    private val binding get() = _binding!!

    var onDeviceSelected: ((name: String, address: String) -> Unit)? = null

    private lateinit var bleAdapter: BleDeviceAdapter
    private var bluetoothAdapter: BluetoothAdapter? = null
    private val scanHandler = Handler(Looper.getMainLooper())
    private var scanning = false

    private val SCAN_TIMEOUT_MS = 15_000L

    // Permissões necessárias por versão do Android
    private val blePermissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        arrayOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT
        )
    } else {
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        if (results.values.all { it }) {
            startScan()
        } else {
            Toast.makeText(requireContext(), R.string.ble_permission_denied, Toast.LENGTH_LONG).show()
            dismiss()
        }
    }

    private val scanCallback = object : ScanCallback() {
        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val device = result.device
            val name = device.name?.takeIf { it.isNotBlank() } ?: "Desconhecido"
            val address = device.address ?: return
            val isPaired = device.bondState == android.bluetooth.BluetoothDevice.BOND_BONDED
            bleAdapter.addDevice(name, address, isPaired)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = DialogBleScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bluetoothAdapter = (requireContext()
            .getSystemService(Context.BLUETOOTH_SERVICE) as? BluetoothManager)
            ?.adapter

        bleAdapter = BleDeviceAdapter { name, address ->
            stopScan()
            onDeviceSelected?.invoke(name, address)
            dismiss()
        }

        binding.recyclerBleDevices.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = bleAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        }

        binding.btnScanAgain.setOnClickListener {
            bleAdapter.clear()
            requestPermissionsAndScan()
        }

        binding.btnBleCancel.setOnClickListener { dismiss() }

        requestPermissionsAndScan()
    }

    private fun requestPermissionsAndScan() {
        val missing = blePermissions.filter {
            ContextCompat.checkSelfPermission(requireContext(), it) != PackageManager.PERMISSION_GRANTED
        }
        if (missing.isEmpty()) {
            startScan()
        } else {
            permissionLauncher.launch(missing.toTypedArray())
        }
    }

    @SuppressLint("MissingPermission")
    private fun startScan() {
        val adapter = bluetoothAdapter
        if (adapter == null || !adapter.isEnabled) {
            Toast.makeText(requireContext(), R.string.ble_disabled, Toast.LENGTH_LONG).show()
            dismiss()
            return
        }
        if (scanning) return
        scanning = true

        binding.progressBleScan.visibility = View.VISIBLE
        binding.textBleScanStatus.text = getString(R.string.ble_scanning)
        binding.btnScanAgain.visibility = View.GONE

        // Carrega primeiro os dispositivos já pareados (BT clássico + BLE pareado)
        adapter.bondedDevices?.forEach { device ->
            val name = device.name?.takeIf { it.isNotBlank() } ?: "Desconhecido"
            bleAdapter.addDevice(name, device.address, isPaired = true)
        }

        val settings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()

        adapter.bluetoothLeScanner?.startScan(null, settings, scanCallback)

        // Para após o timeout
        scanHandler.postDelayed({ stopScan() }, SCAN_TIMEOUT_MS)
    }

    @SuppressLint("MissingPermission")
    private fun stopScan() {
        if (!scanning) return
        scanning = false
        scanHandler.removeCallbacksAndMessages(null)
        bluetoothAdapter?.bluetoothLeScanner?.stopScan(scanCallback)

        if (_binding != null) {
            binding.progressBleScan.visibility = View.GONE
            binding.textBleScanStatus.text = if (bleAdapter.itemCount == 0)
                getString(R.string.ble_no_devices)
            else
                getString(R.string.ble_scan_done, bleAdapter.itemCount)
            binding.btnScanAgain.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        stopScan()
        super.onDestroyView()
        _binding = null
    }
}
