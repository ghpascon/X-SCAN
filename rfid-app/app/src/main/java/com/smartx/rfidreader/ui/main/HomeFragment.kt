package com.smartx.rfidreader.ui.main
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.smartx.rfidreader.R
import com.smartx.rfidreader.RfidApplication
import com.smartx.rfidreader.core.reader.ReaderConnectionState
import com.smartx.rfidreader.databinding.FragmentHomeBinding
import com.google.android.material.snackbar.Snackbar
import com.smartx.rfidreader.ui.main.config.ConfigFragment
import com.smartx.rfidreader.ui.main.radar.RadarFragment
import com.smartx.rfidreader.ui.main.reader.ReaderSelectionFragment
import com.smartx.rfidreader.ui.main.reader.BleScanDialogFragment
import com.smartx.rfidreader.ui.main.reader.ConnectionLogDialogFragment
import com.smartx.rfidreader.readers.ih25.IH25Reader
import com.smartx.rfidreader.readers.tsl1128.Tsl1128Reader
import com.smartx.rfidreader.readers.x714.X714Reader
import com.smartx.rfidreader.ui.main.reading.ReadingFragment
import com.smartx.rfidreader.ui.sync.SyncFragment
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
                        val dialog = BleScanDialogFragment()
                        dialog.onDeviceSelected = { _, address ->
                            when (reader) {
                                is IH25Reader -> reader.targetMacAddress = address
                                is Tsl1128Reader -> reader.targetMacAddress = address
                                is X714Reader -> reader.targetMacAddress = address
                            }
                            viewModel.connect(reader)
                            ConnectionLogDialogFragment()
                                .show(childFragmentManager, "connection_log")
                        }
                        dialog.show(childFragmentManager, "ble_scan")
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
