package com.smartx.rfidreader.ui.selection

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.smartx.rfidreader.R
import com.smartx.rfidreader.core.reader.IRfidReader
import com.smartx.rfidreader.databinding.ActivitySelectionBinding
import com.smartx.rfidreader.readers.ih25.IH25Reader
import com.smartx.rfidreader.readers.tsl1128.Tsl1128Reader
import com.smartx.rfidreader.readers.x714.X714Reader
import com.smartx.rfidreader.readers.zebra.ZebraReader
import com.smartx.rfidreader.ui.base.BaseActivity
import com.smartx.rfidreader.ui.main.MainActivity
import com.smartx.rfidreader.ui.main.reader.BleScanDialogFragment
import kotlinx.coroutines.launch

class ReaderSelectionActivity : BaseActivity<ActivitySelectionBinding>() {

    private val viewModel: ReaderSelectionViewModel by viewModels()
    private lateinit var adapter: ReaderListAdapter

    override fun inflateBinding(inflater: LayoutInflater) = ActivitySelectionBinding.inflate(inflater)

    override fun onActivityReady(savedInstanceState: Bundle?) {
        setupRecyclerView()
        observeState()
    }

    private fun setupRecyclerView() {
        adapter = ReaderListAdapter { reader ->
            onConnectClicked(reader)
        }
        binding.recyclerViewReaders.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewReaders.adapter = adapter
    }

    private fun onConnectClicked(reader: IRfidReader) {
        if (reader is ZebraReader) {
            showZebraTransportDialog(reader)
            return
        }

        if (reader.isBle) {
            showBleScanDialog(reader)
            return
        }

        viewModel.connect(reader)
    }

    private fun showBleScanDialog(reader: IRfidReader) {
        val dialog = BleScanDialogFragment()
        dialog.onDeviceSelected = { _, address ->
            when (reader) {
                is IH25Reader -> reader.targetMacAddress = address
                is Tsl1128Reader -> reader.targetMacAddress = address
                is X714Reader -> reader.targetMacAddress = address
                is ZebraReader -> reader.targetMacAddress = address
            }
            viewModel.connect(reader)
        }
        dialog.show(supportFragmentManager, "ble_scan")
    }

    private fun showZebraTransportDialog(reader: ZebraReader) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.zebra_transport_title)
            .setMessage(R.string.zebra_transport_message)
            .setItems(R.array.zebra_transport_options) { dialog, which ->
                when (which) {
                    0 -> {
                        reader.transportMode = ZebraReader.TransportMode.BLUETOOTH
                        showBleScanDialog(reader)
                    }
                    1 -> {
                        reader.transportMode = ZebraReader.TransportMode.SERIAL
                        reader.targetMacAddress = null
                        viewModel.connect(reader)
                    }
                    else -> dialog.dismiss()
                }
            }
            .show()
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    adapter.submitList(state.readers)

                    val statusText = when {
                        state.isConnecting -> "Conectando..."
                        state.connectedReader != null -> "Conectado"
                        else -> "Desconectado"
                    }
                    val statusDrawable = when {
                        state.connectedReader != null -> com.smartx.rfidreader.R.drawable.ic_status_connected
                        state.isConnecting -> com.smartx.rfidreader.R.drawable.ic_status_connected
                        else -> com.smartx.rfidreader.R.drawable.ic_status_disconnected
                    }
                    updateHeader(
                        state.connectedReader?.displayName
                            ?: getString(com.smartx.rfidreader.R.string.selection_title),
                        statusText,
                        statusDrawable
                    )

                    binding.progressBar.visibility =
                        if (state.isConnecting) View.VISIBLE else View.GONE

                    state.errorMessage?.let { msg ->
                        Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG).show()
                        viewModel.clearError()
                    }

                    state.connectedReader?.let { reader ->
                        val intent = Intent(this@ReaderSelectionActivity, MainActivity::class.java)
                        intent.putExtra("reader_id", reader.readerId)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }
}
