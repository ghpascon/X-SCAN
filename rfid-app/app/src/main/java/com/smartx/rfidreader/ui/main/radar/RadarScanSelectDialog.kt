package com.smartx.rfidreader.ui.main.radar

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.smartx.rfidreader.databinding.DialogRadarScanBinding
import com.smartx.rfidreader.ui.main.MainViewModel
import kotlinx.coroutines.launch

/**
 * BottomSheet expandida que mostra as tags atualmente visíveis e permite
 * selecionar quais adicionar como targets do radar.
 * O gatilho físico também inicia/para o inventário enquanto o dialog está aberto.
 */
class RadarScanSelectDialog(
    private val existingEpcs: Set<String>,
    private val onTagsSelected: (List<String>) -> Unit
) : BottomSheetDialogFragment() {

    companion object {
        private val TRIGGER_KEYCODES = intArrayOf(
            KeyEvent.KEYCODE_F1,
            KeyEvent.KEYCODE_FOCUS,
            293,
            KeyEvent.KEYCODE_HEADSETHOOK,
            KeyEvent.KEYCODE_BUTTON_R1,
            523   // XR2 handle trigger
        )
    }

    private var _binding: DialogRadarScanBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var scanAdapter: RadarScanAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DialogRadarScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Expande o bottom sheet para quase tela cheia
        val parentView = view.parent as? View
        parentView?.post {
            val behavior = BottomSheetBehavior.from(parentView)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.skipCollapsed = true
        }

        // Intercepta o gatilho físico na Window do dialog
        dialog?.setOnKeyListener { _, keyCode, event ->
            if (keyCode in TRIGGER_KEYCODES) {
                when (event.action) {
                    KeyEvent.ACTION_DOWN -> { mainViewModel.onTriggerPressed(); true }
                    KeyEvent.ACTION_UP   -> { mainViewModel.onTriggerReleased(); true }
                    else -> false
                }
            } else false
        }

        scanAdapter = RadarScanAdapter(existingEpcs)
        binding.recyclerViewScan.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewScan.adapter = scanAdapter

        binding.btnToggleScanInventory.setOnClickListener { mainViewModel.toggleInventory() }
        binding.btnSelectAll.setOnClickListener { scanAdapter.selectAll() }
        binding.btnClearScanned.setOnClickListener { mainViewModel.clearTags() }
        binding.btnCancelScan.setOnClickListener { dismissAllowingStateLoss() }
        binding.btnAddSelected.setOnClickListener {
            val epcs = scanAdapter.getSelectedEpcs()
            if (epcs.isNotEmpty()) onTagsSelected(epcs)
            dismissAllowingStateLoss()
        }

        observeState()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    mainViewModel.uiState.collect { state ->
                        binding.btnToggleScanInventory.text =
                            if (state.isInventorying) "Parar Scan" else "Iniciar Scan"
                    }
                }

                launch {
                    mainViewModel.tags.collect { tags ->
                        // Mostra TODAS as tags — o adapter diferencia as já adicionadas
                        binding.textScanCount.text =
                            if (tags.isEmpty()) "" else "${tags.size} tags"
                        if (tags.isEmpty()) {
                            binding.textScanEmpty.visibility = View.VISIBLE
                            binding.recyclerViewScan.visibility = View.GONE
                        } else {
                            binding.textScanEmpty.visibility = View.GONE
                            binding.recyclerViewScan.visibility = View.VISIBLE
                            scanAdapter.submitList(tags)
                        }
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
