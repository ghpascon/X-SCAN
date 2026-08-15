package com.smartx.rfidreader.ui.main.webhook

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
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
import com.smartx.rfidreader.core.settings.AppSettings
import com.smartx.rfidreader.core.webhook.WebhookSendStatus
import com.smartx.rfidreader.databinding.FragmentWebhookBinding
import com.smartx.rfidreader.core.webhook.WebhookService
import com.smartx.rfidreader.core.webhook.WebhookStatusStore
import com.smartx.rfidreader.ui.main.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import androidx.recyclerview.widget.LinearLayoutManager
import java.text.SimpleDateFormat
import java.util.Locale
import kotlinx.coroutines.launch

class WebhookFragment : Fragment() {

    companion object {
        private const val TOGGLE_DEBOUNCE_MS = 700L
        private const val TOGGLE_RECOVERY_MS = 1800L
    }

    private var _binding: FragmentWebhookBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()
    private var lastClearedWebhookAt: Long = -1L
    private var isWebhookRunning: Boolean = false
    private var isSendingNow: Boolean = false
    private var lastWebhookStatus: WebhookSendStatus? = null
    private val timeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    private var lastToggleClickMs: Long = 0L
    private var toggleInFlight: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentWebhookBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Populate fields from settings and observe tags count
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collect { state ->
                        val s = state.appSettings
                        if (binding.inputWebhookUrl.text.isNullOrBlank()) {
                            binding.inputWebhookUrl.setText(s.webhookUrl)
                        }
                        if (binding.inputWebhookInterval.text.isNullOrBlank()) {
                            binding.inputWebhookInterval.setText(s.webhookIntervalSeconds.toString())
                        }
                    }
                }

                launch {
                    viewModel.tags.collect { tags ->
                        val countText = if (tags.isEmpty()) getString(R.string.tag_count_zero)
                        else getString(R.string.tag_count, tags.size)
                        binding.textWebhookTagCount.text = countText
                    }
                }
            }
        }

        binding.btnSaveWebhook.setOnClickListener {
            val settings = buildValidatedSettings(requireUrl = false) ?: return@setOnClickListener
            viewModel.saveAppSettings(settings) {
                Snackbar.make(binding.root, getString(R.string.webhook_saved), Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.btnToggleWebhook.setOnClickListener {
            if (!canToggleWebhookNow()) return@setOnClickListener

            val ctx = requireContext()
            if (isWebhookRunning) {
                isWebhookRunning = false
                lockToggleButton()
                refreshServiceUi()
                refreshMonitoringUi()
                val stopIntent = Intent(ctx, WebhookService::class.java).apply { action = WebhookService.ACTION_STOP }
                ctx.startService(stopIntent)
            } else {
                val settings = buildValidatedSettings(requireUrl = true) ?: return@setOnClickListener
                isWebhookRunning = true
                lockToggleButton()
                refreshServiceUi()
                refreshMonitoringUi()
                viewModel.saveAppSettings(settings) {
                    val startIntent = Intent(ctx, WebhookService::class.java).apply { action = WebhookService.ACTION_START }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        ctx.startForegroundService(startIntent)
                    } else {
                        ctx.startService(startIntent)
                    }
                }
            }
        }

        // Setup history recycler
        val adapter = WebhookStatusAdapter()
        binding.recyclerWebhookHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerWebhookHistory.adapter = adapter

        // Observe sending flag and history
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    WebhookStatusStore.running.collectLatest { running ->
                        isWebhookRunning = running
                        toggleInFlight = false
                        refreshServiceUi()
                        refreshMonitoringUi()
                    }
                }

                launch {
                    WebhookStatusStore.sending.collectLatest { sending ->
                        isSendingNow = sending
                        refreshMonitoringUi()
                    }
                }

                launch {
                    WebhookStatusStore.history.collectLatest { list ->
                        // show latest first
                        adapter.submitList(list.reversed())
                        lastWebhookStatus = list.lastOrNull()

                        val last = lastWebhookStatus
                        if (last != null && last.success && last.sentCount > 0 && last.timestamp.time != lastClearedWebhookAt) {
                            lastClearedWebhookAt = last.timestamp.time
                            viewModel.clearTags()
                        }

                        refreshMonitoringUi()
                    }
                }
            }
        }

        syncRunningStateFromService()
    }

    override fun onResume() {
        super.onResume()
        syncRunningStateFromService()
    }

    private fun buildValidatedSettings(requireUrl: Boolean): AppSettings? {
        val url = binding.inputWebhookUrl.text?.toString()?.trim().orEmpty()
        val intervalText = binding.inputWebhookInterval.text?.toString()?.trim().orEmpty()
        val interval = intervalText.toIntOrNull()

        if (requireUrl && url.isBlank()) {
            Snackbar.make(binding.root, getString(R.string.sync_no_url), Snackbar.LENGTH_SHORT).show()
            return null
        }

        if (url.isNotBlank() && !url.startsWith("http://") && !url.startsWith("https://")) {
            Snackbar.make(binding.root, getString(R.string.error_invalid_url), Snackbar.LENGTH_SHORT).show()
            return null
        }

        if (interval == null || interval < 1) {
            Snackbar.make(binding.root, getString(R.string.error_invalid_webhook_interval), Snackbar.LENGTH_SHORT).show()
            return null
        }

        val settings = viewModel.uiState.value.appSettings
        return settings.copy(webhookUrl = url, webhookIntervalSeconds = interval)
    }

    private fun refreshServiceUi() {
        binding.btnToggleWebhook.text = if (isWebhookRunning) getString(R.string.btn_stop_webhook) else getString(R.string.btn_start_webhook)
        binding.textWebhookStatus.text = if (isWebhookRunning) getString(R.string.webhook_status_active) else getString(R.string.webhook_status_inactive)
        binding.btnToggleWebhook.isEnabled = !toggleInFlight
    }

    private fun syncRunningStateFromService() {
        val running = WebhookService.isRunning
        isWebhookRunning = running
        if (WebhookStatusStore.running.value != running) {
            WebhookStatusStore.setRunning(running)
        }
        refreshServiceUi()
        refreshMonitoringUi()
    }

    private fun refreshMonitoringUi() {
        binding.progressSending.visibility = if (isSendingNow) View.VISIBLE else View.GONE

        val statusText = when {
            isSendingNow -> getString(R.string.label_sending)
            isWebhookRunning -> {
                val last = lastWebhookStatus
                if (last != null) {
                    if (last.success) {
                        getString(R.string.webhook_send_success, last.sentCount, timeFormat.format(last.timestamp))
                    } else {
                        getString(R.string.webhook_send_fail, (last.error ?: "Erro"), timeFormat.format(last.timestamp))
                    }
                } else {
                    getString(R.string.label_sending_waiting)
                }
            }
            else -> getString(R.string.label_sending_inactive)
        }

        binding.textSendingStatus.text = statusText
    }

    private fun canToggleWebhookNow(): Boolean {
        val now = SystemClock.elapsedRealtime()
        if (toggleInFlight) return false
        if (now - lastToggleClickMs < TOGGLE_DEBOUNCE_MS) return false
        lastToggleClickMs = now
        return true
    }

    private fun lockToggleButton() {
        toggleInFlight = true
        refreshServiceUi()

        binding.btnToggleWebhook.postDelayed({
            if (_binding == null) return@postDelayed
            if (toggleInFlight) {
                toggleInFlight = false
                syncRunningStateFromService()
            }
        }, TOGGLE_RECOVERY_MS)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
