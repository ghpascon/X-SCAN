package com.smartx.xscan.ui.main.reader

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
import com.smartx.xscan.R
import com.smartx.rfid.core.IRfidReader
import com.smartx.rfid.core.ReaderConnectionState
import com.smartx.xscan.databinding.FragmentReaderSelectionBinding
import com.smartx.xscan.ui.main.MainViewModel
import com.smartx.xscan.ui.selection.ReaderListAdapter
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

    private fun onConnectClicked(reader: IRfidReader) {
        ReaderConnectFlow.start(this, reader, viewModel)
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
