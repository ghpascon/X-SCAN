package com.smartx.rfidreader.ui.main.reader

import android.content.Context
import android.view.LayoutInflater
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.smartx.rfidreader.R
import com.smartx.rfidreader.databinding.DialogZebraTransportBinding

object ZebraTransportDialog {

    fun show(
        context: Context,
        onBluetoothSelected: () -> Unit,
        onSerialSelected: () -> Unit
    ) {
        val binding = DialogZebraTransportBinding.inflate(LayoutInflater.from(context))
        val dialog = MaterialAlertDialogBuilder(context)
            .setTitle(R.string.zebra_transport_title)
            .setView(binding.root)
            .create()

        binding.textMessage.setText(R.string.zebra_transport_message)

        binding.btnBluetooth.setOnClickListener {
            dialog.dismiss()
            onBluetoothSelected()
        }

        binding.btnSerial.setOnClickListener {
            dialog.dismiss()
            onSerialSelected()
        }

        binding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}