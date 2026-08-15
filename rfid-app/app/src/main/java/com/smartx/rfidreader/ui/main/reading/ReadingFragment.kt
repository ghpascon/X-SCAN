package com.smartx.rfidreader.ui.main.reading

import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.ToneGenerator
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.chip.ChipGroup
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.smartx.rfidreader.R
import com.smartx.rfidreader.core.reader.ReaderConnectionState
import com.smartx.rfidreader.core.reader.RfidTag
import com.smartx.rfidreader.databinding.FragmentReadingBinding
import com.smartx.rfidreader.ui.main.MainViewModel
import java.io.OutputStreamWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class ReadingFragment : Fragment() {

    private var _binding: FragmentReadingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private lateinit var tagAdapter: TagListAdapter

    /** ToneGenerator reutilizado — criado em onStart, liberado em onStop */
    private var toneGenerator: ToneGenerator? = null
    private var pendingInventoryName: String? = null

    // Launcher para solicitar permissão de localização
    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { _ ->
        // Independente do resultado, tenta salvar (GpsHelper lida com permissão negada)
        val inventoryName = pendingInventoryName
        if (!inventoryName.isNullOrBlank()) {
            doSaveInventory(inventoryName)
        }
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
            showInventoryNameDialog()
        }
        binding.btnExportCsv.setOnClickListener {
            exportTagsToCsv()
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

    private fun showInventoryNameDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_inventory_name, null)
        val inputLayout = dialogView.findViewById<TextInputLayout>(R.id.inputLayoutInventoryName)
        val input = dialogView.findViewById<TextInputEditText>(R.id.inputInventoryName)

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.inventory_name_title)
            .setView(dialogView)
            .setNegativeButton(R.string.btn_cancel, null)
            .setPositiveButton(R.string.btn_save_reading, null)
            .create()

        dialog.setOnShowListener {
            input.requestFocus()
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
                val inventoryName = input.text?.toString()?.trim().orEmpty()
                if (inventoryName.isBlank()) {
                    inputLayout.error = getString(R.string.inventory_name_required)
                    return@setOnClickListener
                }
                inputLayout.error = null

                binding.btnSaveReading.isEnabled = false
                dialog.dismiss()
                requestLocationAndSave(inventoryName)
            }
        }

        dialog.show()
    }

    private fun requestLocationAndSave(inventoryName: String) {
        pendingInventoryName = inventoryName
        if (hasLocationPermission()) {
            doSaveInventory(inventoryName)
        } else {
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun doSaveInventory(inventoryName: String) {
        pendingInventoryName = null
        viewModel.saveInventory(inventoryName)
    }

    private fun exportTagsToCsv() {
        val tags = viewModel.tags.value
        if (tags.isEmpty()) {
            Snackbar.make(binding.root, getString(R.string.export_csv_empty), Snackbar.LENGTH_SHORT).show()
            return
        }

        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val fileName = "xscan_tags_${timestamp}.csv"

        val uri = createCsvUri(fileName)
        if (uri == null) {
            Snackbar.make(binding.root, getString(R.string.export_csv_error), Snackbar.LENGTH_SHORT).show()
            return
        }

        val success = runCatching {
            requireContext().contentResolver.openOutputStream(uri)?.use { out ->
                OutputStreamWriter(out, Charsets.UTF_8).use { writer ->
                    writer.write(buildCsvContent(tags))
                }
            } ?: throw IllegalStateException("Não foi possível abrir o arquivo CSV")
        }.isSuccess

        if (success) {
            Snackbar.make(
                binding.root,
                getString(R.string.export_csv_success, fileName),
                Snackbar.LENGTH_LONG
            ).show()
        } else {
            requireContext().contentResolver.delete(uri, null, null)
            Snackbar.make(binding.root, getString(R.string.export_csv_error), Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun createCsvUri(fileName: String): Uri? {
        val values = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, fileName)
            put(MediaStore.Downloads.MIME_TYPE, "text/csv")
            put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }
        return requireContext().contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
    }

    private fun buildCsvContent(tags: List<RfidTag>): String {
        val timeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US)
        val headers = listOf("epc", "tid", "rssi", "antenna", "read_count", "timestamp")

        return buildString {
            appendLine(headers.joinToString(","))
            tags.forEach { tag ->
                val row = listOf(
                    tag.epc,
                    tag.tid,
                    tag.rssi,
                    tag.antenna.toString(),
                    tag.readCount.toString(),
                    timeFormat.format(tag.timestamp)
                ).joinToString(",") { escapeCsv(it) }
                appendLine(row)
            }
        }
    }

    private fun escapeCsv(value: String): String {
        val needsQuotes = value.contains(',') || value.contains('"') || value.contains('\n')
        if (!needsQuotes) return value
        return "\"${value.replace("\"", "\"\"")}\""
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
                            binding.btnExportCsv.isEnabled = false
                        } else {
                            binding.btnToggleInventory.text = getString(R.string.btn_start_inventory)
                            binding.btnSaveReading.isEnabled =
                                connected && viewModel.tags.value.isNotEmpty()
                            binding.btnExportCsv.isEnabled = viewModel.tags.value.isNotEmpty()
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
                        binding.btnExportCsv.isEnabled = total > 0 && notInventorying
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
