package com.smartx.rfidreader.ui.main.reading

import android.Manifest
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import com.smartx.rfidreader.R
import com.smartx.rfidreader.core.reader.ReaderConnectionState
import com.smartx.rfidreader.databinding.FragmentReadingBinding
import com.smartx.rfidreader.ui.main.MainViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class ReadingFragment : Fragment() {

    private var _binding: FragmentReadingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var tagAdapter: TagListAdapter

    /** ToneGenerator reutilizado — criado em onStart, liberado em onStop */
    private var toneGenerator: ToneGenerator? = null

    // Launcher para solicitar permissão de localização
    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { _ ->
        // Independente do resultado, tenta salvar (GpsHelper lida com permissão negada)
        doSaveInventory()
    }

    private fun hasLocationPermission(): Boolean =
        ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    private fun playBeep() {
        toneGenerator?.startTone(ToneGenerator.TONE_PROP_ACK, 100)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        binding.btnSaveReading.setOnClickListener {
            binding.btnSaveReading.isEnabled = false
            requestLocationAndSave()
        }

        binding.chipGroupLimit.setOnCheckedStateChangeListener { _, checkedIds ->
            val limit: Int? = when (checkedIds.firstOrNull()) {
                R.id.chip50  -> 50
                R.id.chip100 -> 100
                R.id.chip200 -> 200
                else         -> null  // chipAll
            }
            viewModel.setDisplayLimit(limit)
        }
    }

    private fun requestLocationAndSave() {
        if (hasLocationPermission()) {
            doSaveInventory()
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun doSaveInventory() {
        viewModel.saveInventory()
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                // Estado principal da UI
                launch {
                    viewModel.uiState.collect { state ->
                        val connected = state.connectionState == ReaderConnectionState.CONNECTED
                        binding.btnToggleInventory.isEnabled = connected
                        binding.btnClearTags.isEnabled = connected

                        if (state.isInventorying) {
                            binding.btnToggleInventory.text = getString(R.string.btn_stop_inventory)
                            binding.btnSaveReading.isEnabled = false
                        } else {
                            binding.btnToggleInventory.text = getString(R.string.btn_start_inventory)
                            binding.btnSaveReading.isEnabled =
                                connected && viewModel.tags.value.isNotEmpty()
                        }
                    }
                }

                // Lista de tags — combina total com limite de exibição
                var prevTotal = 0
                launch {
                    combine(viewModel.tags, viewModel.displayLimit) { allTags, limit ->
                        Pair(allTags, limit)
                    }.collect { (allTags, limit) ->
                        val total = allTags.size
                        val visible = if (limit == null) allTags else allTags.take(limit)

                        tagAdapter.submitList(visible)

                        val suffix = if (limit != null && total > limit) " (${limit})" else ""
                        binding.textTagCount.text = getString(R.string.tag_count, total) + suffix

                        // Rola ao topo apenas na primeira tag (lista vazia → 1ª leitura)
                        if (prevTotal == 0 && total > 0 && visible.isNotEmpty()) {
                            binding.recyclerViewTags.scrollToPosition(0)
                        }
                        prevTotal = if (total == 0) 0 else total

                        val notInventorying = !viewModel.uiState.value.isInventorying
                        val connected =
                            viewModel.uiState.value.connectionState == ReaderConnectionState.CONNECTED
                        binding.btnSaveReading.isEnabled =
                            total > 0 && notInventorying && connected
                    }
                }

                // Evento de buzzer (nova tag detectada)
                launch {
                    viewModel.buzzerEvent.collect {
                        playBeep()
                    }
                }

                // Resultado do salvamento
                launch {
                    viewModel.saveInventoryResult.collect { success ->
                        val msg = if (success)
                            getString(R.string.reading_saved)
                        else
                            getString(R.string.reading_save_error)

                        if (success) {
                            // Mostra snackbar brevemente e volta para a home
                            Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
                            binding.root.postDelayed({
                                parentFragmentManager.popBackStack()
                            }, 1000)
                        } else {
                            // Em caso de erro, reabilita o botão
                            binding.btnSaveReading.isEnabled =
                                viewModel.tags.value.isNotEmpty() &&
                                !viewModel.uiState.value.isInventorying
                            Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        try {
            toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME)
        } catch (_: Exception) {}
    }

    override fun onStop() {
        super.onStop()
        toneGenerator?.release()
        toneGenerator = null
        viewModel.stopInventory()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
