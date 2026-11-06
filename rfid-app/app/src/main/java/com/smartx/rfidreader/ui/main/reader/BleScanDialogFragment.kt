package com.smartx.xscan.ui.main.reader

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartx.rfid.ble.BlePermissions
import com.smartx.rfid.ble.BleScanner
import com.smartx.xscan.R
import com.smartx.xscan.databinding.DialogBleScanBinding
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Diálogo que escaneia dispositivos BLE (via [BleScanner] da biblioteca) e permite ao usuário
 * selecionar um. Retorna o endereço MAC via [onDeviceSelected].
 */
class BleScanDialogFragment : DialogFragment() {

    private var _binding: DialogBleScanBinding? = null
    private val binding get() = _binding!!

    var onDeviceSelected: ((name: String, address: String) -> Unit)? = null

    private lateinit var bleAdapter: BleDeviceAdapter
    private lateinit var scanner: BleScanner
    private var scanJob: Job? = null

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

        scanner = BleScanner(requireContext())

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
        val missing = BlePermissions.missing(requireContext())
        if (missing.isEmpty()) {
            startScan()
        } else {
            permissionLauncher.launch(missing.toTypedArray())
        }
    }

    private fun startScan() {
        if (scanJob?.isActive == true) return
        if (!scanner.isBluetoothEnabled) {
            Toast.makeText(requireContext(), R.string.ble_disabled, Toast.LENGTH_LONG).show()
            dismiss()
            return
        }

        binding.progressBleScan.visibility = View.VISIBLE
        binding.textBleScanStatus.text = getString(R.string.ble_scanning)
        binding.btnScanAgain.visibility = View.GONE

        // O Flow termina sozinho após o timeout do scanner
        scanJob = viewLifecycleOwner.lifecycleScope.launch {
            try {
                scanner.scan().collect { device ->
                    bleAdapter.addDevice(device.name, device.address, device.isPaired)
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("BleScanDialog", "Falha no scan BLE", e)
            } finally {
                showScanFinished()
            }
        }
    }

    private fun stopScan() {
        scanJob?.cancel()
        scanJob = null
    }

    private fun showScanFinished() {
        val b = _binding ?: return
        b.progressBleScan.visibility = View.GONE
        b.textBleScanStatus.text = if (bleAdapter.itemCount == 0)
            getString(R.string.ble_no_devices)
        else
            getString(R.string.ble_scan_done, bleAdapter.itemCount)
        b.btnScanAgain.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        stopScan()
        super.onDestroyView()
        _binding = null
    }
}
