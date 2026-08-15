package com.smartx.rfidreader.ui.main.reader

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.smartx.rfidreader.R
import com.smartx.rfidreader.databinding.DialogZebraTransportBinding

object ZebraTransportDialog {

    private const val TAG = "ZebraTransportDialog"

    fun show(
        context: Context,
        onBluetoothSelected: () -> Unit,
        onSerialSelected: () -> Unit
    ) {
        val activity = context as? Activity
        if (activity?.isFinishing == true || activity?.isDestroyed == true) {
            Log.w(TAG, "Ignoring dialog request because activity is not valid")
            return
        }

        runCatching {
            showCustomDialog(context, onBluetoothSelected, onSerialSelected)
        }.onFailure { error ->
            Log.e(TAG, "Custom Zebra dialog failed, using fallback list dialog", error)
            showFallbackListDialog(context, onBluetoothSelected, onSerialSelected)
        }
    }

    private fun showCustomDialog(
        context: Context,
        onBluetoothSelected: () -> Unit,
        onSerialSelected: () -> Unit
    ) {
        val binding = DialogZebraTransportBinding.inflate(LayoutInflater.from(context))
        val dialog = MaterialAlertDialogBuilder(context)
            .setTitle(R.string.zebra_transport_title)
            .setView(binding.root)
            .setCancelable(true)
            .create()

        binding.textMessage.setText(R.string.zebra_transport_message)

        binding.btnBluetooth.setOnClickListener {
            dialog.dismiss()
            runCatching(onBluetoothSelected)
                .onFailure { error -> Log.e(TAG, "Bluetooth option failed", error) }
        }

        binding.btnSerial.setOnClickListener {
            dialog.dismiss()
            runCatching(onSerialSelected)
                .onFailure { error -> Log.e(TAG, "Serial option failed", error) }
        }

        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.setOnShowListener {
            binding.root.post {
                if (!hasUsableButtons(binding)) {
                    Log.w(TAG, "Custom Zebra dialog buttons are not visible, switching to fallback list")
                    dialog.dismiss()
                    showFallbackListDialog(context, onBluetoothSelected, onSerialSelected)
                }
            }
        }

        dialog.show()
    }

    private fun hasUsableButtons(binding: DialogZebraTransportBinding): Boolean {
        val buttons = arrayOf(binding.btnBluetooth, binding.btnSerial, binding.btnCancel)
        return buttons.all { btn ->
            btn.visibility == View.VISIBLE && btn.alpha > 0f && btn.width > 0 && btn.height > 0
        }
    }

    private fun showFallbackListDialog(
        context: Context,
        onBluetoothSelected: () -> Unit,
        onSerialSelected: () -> Unit
    ) {
        runCatching {
            MaterialAlertDialogBuilder(context)
                .setTitle(R.string.zebra_transport_title)
                .setMessage(R.string.zebra_transport_message)
                .setItems(R.array.zebra_transport_options) { dialog, which ->
                    when (which) {
                        0 -> runCatching(onBluetoothSelected)
                            .onFailure { error -> Log.e(TAG, "Fallback Bluetooth option failed", error) }
                        1 -> runCatching(onSerialSelected)
                            .onFailure { error -> Log.e(TAG, "Fallback Serial option failed", error) }
                        else -> dialog.dismiss()
                    }
                }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
        }.onFailure { error ->
            Log.e(TAG, "Fallback Zebra dialog failed", error)
        }
    }
}