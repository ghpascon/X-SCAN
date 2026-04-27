package com.smartx.rfidreader.ui.main.reader

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.smartx.rfidreader.R
import com.smartx.rfidreader.core.reader.ReaderConnectionState
import com.smartx.rfidreader.databinding.DialogConnectionLogBinding
import com.smartx.rfidreader.ui.main.MainViewModel
import kotlinx.coroutines.launch

/**
 * Dialog que exibe logs em tempo real durante a conexão com o leitor.
 * Observa [MainViewModel.connectionLog] e [MainViewModel.uiState].
 */
class ConnectionLogDialogFragment : DialogFragment() {

    private var _binding: DialogConnectionLogBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = DialogConnectionLogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        isCancelable = false
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogClose.setOnClickListener { dismiss() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                // Atualiza o texto do log a cada nova linha
                launch {
                    viewModel.connectionLog.collect { lines ->
                        binding.textLog.text = lines.joinToString("\n")
                        binding.scrollLog.post {
                            binding.scrollLog.fullScroll(View.FOCUS_DOWN)
                        }
                    }
                }

                // Reage ao estado de conexão
                launch {
                    viewModel.uiState.collect { state ->
                        val connecting = state.isConnecting
                        binding.progressConnect.visibility =
                            if (connecting) View.VISIBLE else View.GONE

                        if (!connecting) {
                            isCancelable = true
                            binding.btnLogClose.isEnabled = true
                            binding.textLogTitle.text = when (state.connectionState) {
                                ReaderConnectionState.CONNECTED ->
                                    getString(R.string.connection_log_connected)
                                else ->
                                    getString(R.string.connection_log_failed)
                            }
                        } else {
                            binding.btnLogClose.isEnabled = false
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
