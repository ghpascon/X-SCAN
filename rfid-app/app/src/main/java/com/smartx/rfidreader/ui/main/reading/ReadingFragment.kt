package com.smartx.rfidreader.ui.main.reading

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
import com.smartx.rfidreader.R
import com.smartx.rfidreader.core.reader.ReaderConnectionState
import com.smartx.rfidreader.databinding.FragmentReadingBinding
import com.smartx.rfidreader.ui.main.MainViewModel
import kotlinx.coroutines.launch

/**
 * Aba de leitura de tags RFID.
 * Mostra a lista de EPCs lidos com RSSI e contador de repetições.
 * A leitura é disparada pelo gatilho físico OU pelo botão na tela.
 */
class ReadingFragment : Fragment() {

    private var _binding: FragmentReadingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var tagAdapter: TagListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentReadingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupButtons()
        observeState()
    }

    private fun setupRecyclerView() {
        tagAdapter = TagListAdapter()
        binding.recyclerViewTags.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewTags.adapter = tagAdapter
    }

    private fun setupButtons() {
        binding.btnToggleInventory.setOnClickListener {
            viewModel.toggleInventory()
        }
        binding.btnClearTags.setOnClickListener {
            viewModel.clearTags()
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        val connected = state.connectionState == ReaderConnectionState.CONNECTED
                        binding.btnToggleInventory.isEnabled = connected
                        binding.btnClearTags.isEnabled = connected

                        if (state.isInventorying) {
                            binding.btnToggleInventory.text = getString(R.string.btn_stop_inventory)
                            binding.inventoryStatusIndicator.visibility = View.VISIBLE
                        } else {
                            binding.btnToggleInventory.text = getString(R.string.btn_start_inventory)
                            binding.inventoryStatusIndicator.visibility = View.GONE
                        }

                        binding.textConnectionStatus.text = when (state.connectionState) {
                            ReaderConnectionState.CONNECTED ->
                                getString(R.string.status_connected, viewModel.reader?.displayName ?: "")
                            ReaderConnectionState.CONNECTING -> getString(R.string.status_connecting)
                            else -> getString(R.string.status_disconnected)
                        }
                    }
                }
                launch {
                    viewModel.tags.collect { tags ->
                        tagAdapter.submitList(tags)
                        binding.textTagCount.text = getString(R.string.tag_count, tags.size)
                        if (tags.isNotEmpty()) {
                            binding.recyclerViewTags.scrollToPosition(0)
                        }
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        // Para a leitura e limpa tags ao sair da aba ou da Activity
        viewModel.stopInventoryAndClear()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
