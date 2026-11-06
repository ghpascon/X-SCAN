package com.smartx.xscan.ui.main
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.smartx.xscan.R
import com.smartx.xscan.RfidApplication
import com.smartx.rfid.core.ReaderConnectionState
import com.smartx.xscan.core.webhook.WebhookService
import com.smartx.xscan.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import com.smartx.xscan.ui.main.config.ConfigFragment
import com.smartx.xscan.ui.main.radar.RadarFragment
import com.smartx.xscan.ui.main.reader.ReaderSelectionFragment
import com.smartx.xscan.ui.main.reader.ConnectionLogDialogFragment
import com.smartx.xscan.ui.main.reader.ReaderConnectFlow
import com.smartx.xscan.ui.main.reading.ReadingFragment
import com.smartx.xscan.ui.sync.SyncFragment
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupNavCards()
        observeState()
        // Tenta reconectar ao último leitor apenas na primeira criação da tela
        if (savedInstanceState == null) {
            viewModel.autoConnectLastReader()
        }
    }

    override fun onResume() {
        super.onResume()
        stopWebhookSendingOnHome()
    }

    private fun stopWebhookSendingOnHome() {
        if (!WebhookService.isRunning) return
        val stopIntent = Intent(requireContext(), WebhookService::class.java).apply {
            action = WebhookService.ACTION_STOP
        }
        requireContext().startService(stopIntent)
    }

    private fun setupNavCards() {
        binding.cardNavReader.setOnClickListener {
            (requireActivity() as MainActivity).navigateTo(ReaderSelectionFragment())
        }
        binding.cardNavConfig.setOnClickListener {
            (requireActivity() as MainActivity).navigateTo(ConfigFragment())
        }
        binding.cardNavReading.setOnClickListener {
            val connected =
                viewModel.uiState.value.connectionState == ReaderConnectionState.CONNECTED
            if (connected) {
                viewModel.clearTags()
                (requireActivity() as MainActivity).navigateTo(ReadingFragment())
            } else {
                // Redireciona para o leitor se não estiver conectado
                (requireActivity() as MainActivity).navigateTo(ReaderSelectionFragment())
            }
        }
        binding.cardNavSync.setOnClickListener {
            (requireActivity() as MainActivity).navigateTo(SyncFragment())
        }
        binding.cardNavWebhook.setOnClickListener {
            (requireActivity() as MainActivity).navigateTo(com.smartx.xscan.ui.main.webhook.WebhookFragment())
        }
        binding.cardNavRadar.setOnClickListener {
            val connected =
                viewModel.uiState.value.connectionState == ReaderConnectionState.CONNECTED
            if (connected) {
                (requireActivity() as MainActivity).navigateTo(RadarFragment())
            } else {
                Snackbar.make(binding.root, getString(R.string.error_no_reader), Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                // Status de conexão no banner (e fecha modais ao conectar)
                launch {
                    viewModel.uiState.collect { state ->
                        val connected = state.connectionState == ReaderConnectionState.CONNECTED
                        val connecting = state.isConnecting

                        // Hint no card de leitura
                        binding.textReadingStatusHint.text = if (connected)
                            getString(R.string.nav_reading_desc)
                        else
                            getString(R.string.nav_reading_no_reader)

                        // Opacidade do card de leitura
                        binding.cardNavReading.alpha = if (connected) 1f else 0.6f

                        // Se conectado, fecha quaisquer modais de conexão/scan abertos
                        if (connected) {
                            try {
                                val ble = childFragmentManager.findFragmentByTag("ble_scan")
                                if (ble != null && ble is androidx.fragment.app.DialogFragment) {
                                    ble.dismissAllowingStateLoss()
                                }
                            } catch (_: Exception) {}
                            try {
                                val log = childFragmentManager.findFragmentByTag("connection_log")
                                if (log != null && log is androidx.fragment.app.DialogFragment) {
                                    log.dismissAllowingStateLoss()
                                }
                            } catch (_: Exception) {}
                        }
                    }
                }

                // Badge de pendentes no card de sync
                launch {
                    val app = requireActivity().application as RfidApplication
                    app.eventRepository.pendingCountFlow.collect { count ->
                        if (count > 0) {
                            binding.badgePending.visibility = View.VISIBLE
                            binding.badgePending.text = if (count > 99) "99+" else count.toString()
                            binding.textSyncPending.text = resources.getQuantityString(
                                R.plurals.pending_events_count, count, count
                            )
                        } else {
                            binding.badgePending.visibility = View.GONE
                            binding.textSyncPending.text = getString(R.string.nav_sync_desc)
                        }
                    }
                }

                // Mostrar modal de scan BLE quando solicitado pelo ViewModel
                launch {
                    viewModel.showBleScanDialog.collect { readerId ->
                        val reader = viewModel.availableReaders.firstOrNull { it.readerId == readerId } ?: return@collect
                        val option = reader.connectionOptions.firstOrNull { it.requiresDevice }
                        ReaderConnectFlow.connectWith(this@HomeFragment, reader, viewModel, option)
                    }
                }
                // Abrir diálogo de logs quando solicitado (auto-connect)
                launch {
                    viewModel.showConnectionLog.collect {
                        ConnectionLogDialogFragment()
                            .show(childFragmentManager, "connection_log")
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
