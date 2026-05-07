package com.smartx.rfidreader.ui.sync

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.smartx.rfidreader.R
import com.smartx.rfidreader.core.db.EventEntity
import com.smartx.rfidreader.databinding.FragmentSyncBinding
import kotlinx.coroutines.launch
import org.json.JSONObject

class SyncFragment : Fragment() {

    private var _binding: FragmentSyncBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SyncViewModel by viewModels()
    private lateinit var eventAdapter: EventListAdapter
    private lateinit var progressAdapter: SyncProgressAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSyncBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEventsList()
        setupProgressList()
        setupWebhookSection()
        setupSyncButton()
        observeState()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Setup
    // ─────────────────────────────────────────────────────────────────────────

    private fun setupEventsList() {
        eventAdapter = EventListAdapter(
            onDelete = { event -> confirmDelete(event) },
            onItemClick = { event -> showJsonDialog(event) }
        )
        binding.recyclerViewEvents.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = eventAdapter
            isNestedScrollingEnabled = false
        }
        binding.btnDeleteAll.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.delete_all_title))
                .setMessage(getString(R.string.delete_all_message))
                .setPositiveButton(getString(R.string.confirm_delete)) { _, _ ->
                    viewModel.deleteAllEvents()
                }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
        }
    }

    private fun setupProgressList() {
        progressAdapter = SyncProgressAdapter()
        binding.recyclerViewProgress.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = progressAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun setupWebhookSection() {
        binding.btnWebhookConfig.setOnClickListener {
            val dlg = WebhookConfigDialogFragment()
            dlg.show(childFragmentManager, "webhook_config")
        }
    }

    private fun setupSyncButton() {
        binding.btnStartSync.setOnClickListener {
            viewModel.startSyncWithProgress(onNoUrl = {
                Snackbar.make(binding.root, getString(R.string.sync_no_url), Snackbar.LENGTH_SHORT).show()
            })
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Actions
    // ─────────────────────────────────────────────────────────────────────────

    private fun confirmDelete(event: EventEntity) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.delete_event_title))
            .setMessage(getString(R.string.delete_event_message))
            .setPositiveButton(getString(R.string.confirm_delete)) { _, _ ->
                viewModel.deleteEvent(event)
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    private fun showJsonDialog(event: EventEntity) {
        val pretty = runCatching {
            JSONObject(event.toWebhookJson()).toString(2)
        }.getOrElse { event.toWebhookJson() }

        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.event_json_title))
            .setMessage(pretty)
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Observadores
    // ─────────────────────────────────────────────────────────────────────────

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {

                launch {
                    viewModel.uiState.collect { state ->
                        val text = if (state.webhookUrl.isBlank())
                            getString(R.string.webhook_not_configured)
                        else
                            state.webhookUrl
                        if (binding.textWebhookSummary.text.toString() != text) {
                            binding.textWebhookSummary.text = text
                        }
                    }
                }

                launch {
                    viewModel.syncProgress.collect { state ->
                        val isActive = state.isRunning || state.items.isNotEmpty()
                        binding.layoutSyncProgress.visibility =
                            if (isActive) View.VISIBLE else View.GONE

                        binding.btnStartSync.isEnabled = !state.isRunning

                        if (isActive) {
                            binding.textSyncProgressLabel.text = if (state.isRunning) {
                                "Sincronizando ${state.current}/${state.total}..."
                            } else {
                                "Concluído ${state.current}/${state.total}"
                            }

                            if (state.total > 0) {
                                binding.progressSync.isIndeterminate = false
                                binding.progressSync.max = state.total
                                binding.progressSync.setProgressCompat(state.current, true)
                            } else {
                                binding.progressSync.isIndeterminate = true
                            }

                            progressAdapter.submitList(state.items)

                            if (!state.isRunning && state.finalMessage != null) {
                                binding.cardFinalResult.visibility = View.VISIBLE
                                binding.textFinalResult.text = state.finalMessage
                            } else {
                                binding.cardFinalResult.visibility = View.GONE
                            }
                        }
                    }
                }

                launch {
                    viewModel.events.collect { events ->
                        eventAdapter.submitList(events)
                        val hasEvents = events.isNotEmpty()
                        binding.textEventsEmpty.visibility = if (!hasEvents) View.VISIBLE else View.GONE
                        binding.recyclerViewEvents.visibility = if (hasEvents) View.VISIBLE else View.GONE
                        binding.btnDeleteAll.isEnabled = hasEvents
                    }
                }

                launch {
                    viewModel.pendingCount.collect { count ->
                        binding.btnStartSync.isEnabled =
                            count > 0 && !viewModel.syncProgress.value.isRunning
                        binding.textPendingCount.text = when {
                            count == 0 -> getString(R.string.no_pending_events)
                            else -> resources.getQuantityString(
                                R.plurals.pending_events_count, count, count
                            )
                        }
                    }
                }
            }
        }
    }
}
