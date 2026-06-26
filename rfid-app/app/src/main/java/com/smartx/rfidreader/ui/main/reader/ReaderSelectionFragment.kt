package com.smartx.rfidreader.ui.main.reader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.smartx.rfidreader.R
import com.smartx.rfidreader.core.reader.ReaderConnectionState
import com.smartx.rfidreader.readers.ih25.IH25Reader
import com.smartx.rfidreader.readers.tsl1128.Tsl1128Reader
import com.smartx.rfidreader.readers.x714.X714Reader
import com.smartx.rfidreader.databinding.FragmentReaderSelectionBinding
import com.smartx.rfidreader.ui.main.MainViewModel
import com.smartx.rfidreader.ui.selection.ReaderListAdapter
import kotlinx.coroutines.launch

class ReaderSelectionFragment : Fragment() {

    private var _binding: FragmentReaderSelectionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var adapter: ReaderListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReaderSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        binding.btnDisconnect.setOnClickListener { viewModel.disconnect() }
        observeState()
    }

    private fun setupRecyclerView() {
        adapter = ReaderListAdapter { reader -> onConnectClicked(reader) }
        binding.recyclerViewReaders.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewReaders.adapter = adapter
        adapter.submitList(viewModel.availableReaders)
    }

    private fun onConnectClicked(reader: com.smartx.rfidreader.core.reader.IRfidReader) {
        if (reader.isBle) {
            // Leitores BLE: mostra escaner antes de conectar
            val dialog = BleScanDialogFragment()
            dialog.onDeviceSelected = { _, address ->
                // Repassa o MAC para qualquer leitor BLE
                when (reader) {
                    is IH25Reader -> reader.targetMacAddress = address
                    is Tsl1128Reader -> reader.targetMacAddress = address
                        is X714Reader -> reader.targetMacAddress = address
                        is com.smartx.rfidreader.readers.zebra.ZebraReader -> reader.targetMacAddress = address
                }
                viewModel.connect(reader)
                // Abre o log de conexão para todos os leitores
                ConnectionLogDialogFragment()
                    .show(childFragmentManager, "connection_log")
            }
            dialog.show(childFragmentManager, "ble_scan")
        } else {
            viewModel.connect(reader)
            ConnectionLogDialogFragment()
                .show(childFragmentManager, "connection_log")
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    val connected = state.connectionState == ReaderConnectionState.CONNECTED
                    val connecting = state.isConnecting

                    binding.progressBar.visibility = if (connecting) View.VISIBLE else View.GONE
                    binding.btnDisconnect.visibility = if (connected) View.VISIBLE else View.GONE

                    adapter.connectedReaderId = if (connected) viewModel.reader?.readerId ?: "" else ""

                    binding.textConnectionStatus.text = when {
                        connecting -> getString(R.string.status_connecting)
                        connected -> getString(R.string.status_connected, viewModel.reader?.displayName ?: "")
                        else -> getString(R.string.reader_selection_hint)
                    }

                    val iconRes = when (state.connectionState) {
                        ReaderConnectionState.CONNECTED -> R.drawable.ic_status_active
                        else -> null
                    }
                    if (iconRes != null) {
                        binding.iconStatus.setImageResource(iconRes)
                        binding.iconStatus.visibility = View.VISIBLE
                    } else {
                        binding.iconStatus.visibility = View.GONE
                    }

                    state.errorMessage?.let { msg ->
                        Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG).show()
                        viewModel.clearError()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
