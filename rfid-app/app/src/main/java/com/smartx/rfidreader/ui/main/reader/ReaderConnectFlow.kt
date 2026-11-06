package com.smartx.xscan.ui.main.reader

import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.smartx.rfid.core.ConnectionOption
import com.smartx.rfid.core.ConnectionRequest
import com.smartx.rfid.core.IRfidReader
import com.smartx.xscan.R
import com.smartx.xscan.ui.main.MainViewModel

/**
 * Fluxo genérico de conexão, guiado por `IRfidReader.connectionOptions`:
 *  1. se o leitor oferece mais de uma forma de conexão, pergunta qual usar;
 *  2. se a opção exige dispositivo (ex.: Bluetooth), abre o scan BLE;
 *  3. conecta e mostra o log.
 *
 * Nenhum modelo de leitor é citado aqui — novos leitores da biblioteca funcionam sem alterações.
 */
object ReaderConnectFlow {

    fun start(fragment: Fragment, reader: IRfidReader, viewModel: MainViewModel) {
        val options = reader.connectionOptions
        if (options.size <= 1) {
            connectWith(fragment, reader, viewModel, options.firstOrNull())
            return
        }
        MaterialAlertDialogBuilder(fragment.requireContext())
            .setTitle(fragment.getString(R.string.connection_option_title, reader.displayName))
            .setItems(options.map { it.label }.toTypedArray()) { _, which ->
                connectWith(fragment, reader, viewModel, options[which])
            }
            .setNegativeButton(R.string.btn_cancel, null)
            .show()
    }

    fun connectWith(
        fragment: Fragment,
        reader: IRfidReader,
        viewModel: MainViewModel,
        option: ConnectionOption?
    ) {
        val needsDevice = option?.requiresDevice ?: reader.isBle
        if (!needsDevice) {
            connect(fragment, reader, viewModel, ConnectionRequest(option))
            return
        }
        BleScanDialogFragment().apply {
            onDeviceSelected = { _, address ->
                connect(fragment, reader, viewModel, ConnectionRequest(option, address))
            }
        }.show(fragment.childFragmentManager, "ble_scan")
    }

    private fun connect(
        fragment: Fragment,
        reader: IRfidReader,
        viewModel: MainViewModel,
        request: ConnectionRequest
    ) {
        viewModel.connect(reader, request)
        ConnectionLogDialogFragment().show(fragment.childFragmentManager, "connection_log")
    }
}
