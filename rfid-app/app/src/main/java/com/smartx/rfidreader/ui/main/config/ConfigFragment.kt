package com.smartx.rfidreader.ui.main.config

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.slider.Slider
import com.smartx.rfidreader.R
import com.smartx.rfidreader.core.reader.ReaderConfig
import com.smartx.rfidreader.core.reader.ReaderConnectionState
import com.smartx.rfidreader.core.settings.AppSettings
import com.smartx.rfidreader.databinding.FragmentConfigBinding
import com.smartx.rfidreader.ui.main.MainViewModel
import kotlinx.coroutines.launch

class ConfigFragment : Fragment() {

    private var _binding: FragmentConfigBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    // Lista local de prefixos em edição
    private val currentPrefixes = mutableListOf<String>()
    private var isUpdatingFromState = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfigBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupReaderConfigSection()
        setupAppSettingsSection()
        observeState()
    }

    // -------------------------------------------------------------------------
    // Seção: Configurações do Leitor
    // -------------------------------------------------------------------------

    private fun setupReaderConfigSection() {
        binding.btnApplyConfig.setOnClickListener {
            val config = buildReaderConfigFromInputs() ?: return@setOnClickListener
            viewModel.saveConfig(config)
        }
        binding.btnReloadConfig.setOnClickListener {
            viewModel.loadConfig()
        }
    }

    private fun buildReaderConfigFromInputs(): ReaderConfig? {
        val power = binding.editPower.text.toString().toIntOrNull() ?: run {
            showSnackbar(getString(R.string.error_invalid_power))
            return null
        }
        if (power !in 5..33) {
            showSnackbar(getString(R.string.error_power_range))
            return null
        }
        val session = binding.spinnerSession.selectedItemPosition
        return ReaderConfig(txPower = power, session = session)
    }

    // -------------------------------------------------------------------------
    // Seção: Configurações do Aplicativo
    // -------------------------------------------------------------------------

    private fun setupAppSettingsSection() {
        // Slider RSSI
        binding.sliderRssi.addOnChangeListener { _, value, fromUser ->
            binding.textRssiValue.text = "${value.toInt()} dBm"
        }

        // Buzzer – salva imediatamente ao alternar
        binding.switchBuzzer.setOnCheckedChangeListener { _, _ ->
            if (!isUpdatingFromState) saveAppSettings()
        }

        // Adicionar prefixo via botão
        binding.btnAddPrefix.setOnClickListener { addPrefixFromInput() }

        // Adicionar prefixo via teclado (Enter/Done)
        binding.editPrefix.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addPrefixFromInput()
                true
            } else false
        }

        // Limpar prefixos
        binding.btnClearPrefixes.setOnClickListener {
            currentPrefixes.clear()
            renderPrefixChips()
            saveAppSettings()
        }

        // Salvar configurações do app
        binding.btnSaveAppSettings.setOnClickListener { saveAppSettings() }
    }

    private fun addPrefixFromInput() {
        val prefix = binding.editPrefix.text.toString().trim().uppercase()
        if (prefix.isBlank()) return
        if (prefix in currentPrefixes) {
            showSnackbar(getString(R.string.prefix_already_exists))
            return
        }
        currentPrefixes.add(prefix)
        binding.editPrefix.text?.clear()
        renderPrefixChips()
        saveAppSettings()
    }

    private fun renderPrefixChips() {
        binding.chipGroupPrefixes.removeAllViews()
        currentPrefixes.forEachIndexed { index, prefix ->
            val chip = Chip(requireContext()).apply {
                text = prefix
                isCloseIconVisible = true
                setOnCloseIconClickListener {
                    currentPrefixes.removeAt(index)
                    renderPrefixChips()
                    saveAppSettings()
                }
            }
            binding.chipGroupPrefixes.addView(chip)
        }
    }

    private fun saveAppSettings() {
        val rssi = binding.sliderRssi.value.toInt()
        val buzzer = binding.switchBuzzer.isChecked
        val current = viewModel.uiState.value.appSettings
        viewModel.saveAppSettings(
            current.copy(
                buzzerEnabled = buzzer,
                rssiFilter = rssi,
                prefixes = currentPrefixes.toList()
            )
        )
        showSnackbar(getString(R.string.app_settings_saved))
    }

    // -------------------------------------------------------------------------
    // Observer
    // -------------------------------------------------------------------------

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        updateReaderSection(state)
                        updateAppSettingsSection(state.appSettings)
                        handleConfigSaveResult(state)
                    }
                }
            }
        }
    }

    private fun updateReaderSection(state: com.smartx.rfidreader.ui.main.MainUiState) {
        val connected = state.connectionState == ReaderConnectionState.CONNECTED
        binding.btnApplyConfig.isEnabled = connected && !state.isSavingConfig
        binding.btnReloadConfig.isEnabled = connected && !state.isSavingConfig
        binding.progressSaving.visibility = if (state.isSavingConfig) View.VISIBLE else View.GONE

        if (!state.isSavingConfig) {
            binding.editPower.setText(state.config.txPower.toString())
            binding.spinnerSession.setSelection(state.config.session.coerceIn(0, 3))
        }
    }

    private fun updateAppSettingsSection(settings: AppSettings) {
        isUpdatingFromState = true
        val rssiValue = settings.rssiFilter.toFloat().coerceIn(-120f, -30f)
        binding.sliderRssi.value = rssiValue
        binding.textRssiValue.text = "${rssiValue.toInt()} dBm"
        binding.switchBuzzer.isChecked = settings.buzzerEnabled

        if (currentPrefixes != settings.prefixes) {
            currentPrefixes.clear()
            currentPrefixes.addAll(settings.prefixes)
            renderPrefixChips()
        }
        isUpdatingFromState = false
    }

    private fun handleConfigSaveResult(state: com.smartx.rfidreader.ui.main.MainUiState) {
        state.configSaveSuccess?.let { ok ->
            val msg = if (ok) getString(R.string.config_saved) else getString(R.string.config_save_error)
            showSnackbar(msg)
            viewModel.consumeConfigSaveResult()
        }
    }

    private fun showSnackbar(msg: String) {
        Snackbar.make(requireActivity().window.decorView, msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
