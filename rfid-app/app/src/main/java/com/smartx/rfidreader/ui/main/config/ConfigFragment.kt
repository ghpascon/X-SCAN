package com.smartx.rfidreader.ui.main.config

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.snackbar.Snackbar
import com.smartx.rfidreader.R
import com.smartx.rfidreader.core.reader.ReaderConfig
import com.smartx.rfidreader.core.reader.ReaderConnectionState
import com.smartx.rfidreader.databinding.FragmentConfigBinding
import com.smartx.rfidreader.ui.main.MainViewModel
import kotlinx.coroutines.launch

/**
 * Aba de configurações do leitor RFID.
 * Exibe e permite editar: Potência TX, Session Gen2, Filtro RSSI, Região, Modo de inventário.
 */
class ConfigFragment : Fragment() {

    private var _binding: FragmentConfigBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentConfigBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUi()
        observeState()
    }

    private fun setupUi() {
        binding.btnApplyConfig.setOnClickListener {
            val config = buildConfigFromInputs() ?: return@setOnClickListener
            viewModel.saveConfig(config)
        }

        binding.btnReloadConfig.setOnClickListener {
            viewModel.loadConfig()
        }
    }

    private fun buildConfigFromInputs(): ReaderConfig? {
        val power = binding.editPower.text.toString().toIntOrNull() ?: run {
            showError(getString(R.string.error_invalid_power))
            return null
        }
        if (power !in 5..33) {
            showError(getString(R.string.error_power_range))
            return null
        }
        val session = binding.spinnerSession.selectedItemPosition
        val rssiFilter = binding.editRssiFilter.text.toString().toIntOrNull() ?: -120
        return ReaderConfig(
            txPower = power,
            session = session,
            rssiFilter = rssiFilter
        )
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    val connected = state.connectionState == ReaderConnectionState.CONNECTED

                    binding.btnApplyConfig.isEnabled = connected && !state.isSavingConfig
                    binding.btnReloadConfig.isEnabled = connected && !state.isSavingConfig
                    binding.progressSaving.visibility =
                        if (state.isSavingConfig) View.VISIBLE else View.GONE

                    // Preenche campos com config atual
                    if (!state.isSavingConfig) {
                        binding.editPower.setText(state.config.txPower.toString())
                        binding.spinnerSession.setSelection(state.config.session.coerceIn(0, 3))
                        binding.editRssiFilter.setText(state.config.rssiFilter.toString())
                    }

                    state.configSaveSuccess?.let {
                        val msg = if (it) getString(R.string.config_saved) else getString(R.string.config_save_error)
                        Snackbar.make(requireActivity().window.decorView, msg, Snackbar.LENGTH_SHORT).show()
                        viewModel.consumeConfigSaveResult()
                    }
                }
            }
        }
    }

    private fun showError(msg: String) {
        Snackbar.make(requireActivity().window.decorView, msg, Snackbar.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
