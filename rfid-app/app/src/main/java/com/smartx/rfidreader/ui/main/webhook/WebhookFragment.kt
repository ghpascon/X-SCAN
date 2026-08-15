package com.smartx.rfidreader.ui.main.webhook

import android.content.Context
import android.content.Intent
import android.os.Build
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
import com.smartx.rfidreader.databinding.FragmentWebhookBinding
import com.smartx.rfidreader.core.webhook.WebhookService
import com.smartx.rfidreader.core.webhook.WebhookStatusStore
import com.smartx.rfidreader.ui.main.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import androidx.recyclerview.widget.LinearLayoutManager
import com.smartx.rfidreader.ui.main.webhook.WebhookStatusAdapter
import java.text.SimpleDateFormat
import java.util.Locale
import kotlinx.coroutines.launch

class WebhookFragment : Fragment() {

    private var _binding: FragmentWebhookBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by activityViewModels()

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
            val url = binding.inputWebhookUrl.text?.toString()?.trim() ?: ""
            val interval = binding.inputWebhookInterval.text?.toString()?.toIntOrNull() ?: 30
            val settings = viewModel.uiState.value.appSettings
            val newSettings = settings.copy(webhookUrl = url, webhookIntervalSeconds = interval)
            viewModel.saveAppSettings(newSettings)
            Snackbar.make(binding.root, getString(R.string.webhook_saved), Snackbar.LENGTH_SHORT).show()
        }

        binding.btnToggleWebhook.setOnClickListener {
            val ctx = requireContext()
            if (WebhookService.isRunning) {
                val stopIntent = Intent(ctx, WebhookService::class.java).apply { action = WebhookService.ACTION_STOP }
                ctx.stopService(stopIntent)
                binding.textWebhookStatus.text = "Webhook ativo: Não"
                binding.btnToggleWebhook.text = getString(R.string.btn_start_webhook)
            } else {
                val startIntent = Intent(ctx, WebhookService::class.java).apply { action = WebhookService.ACTION_START }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    ctx.startForegroundService(startIntent)
                } else {
                    ctx.startService(startIntent)
                }
                binding.textWebhookStatus.text = "Webhook ativo: Sim"
                binding.btnToggleWebhook.text = getString(R.string.btn_stop_webhook)
            }
        }

        // Setup history recycler
        val adapter = WebhookStatusAdapter()
        binding.recyclerWebhookHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerWebhookHistory.adapter = adapter

        val timeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        // Observe sending flag and history
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    WebhookStatusStore.sending.collectLatest { sending ->
                        binding.progressSending.visibility = if (sending) View.VISIBLE else View.GONE
                        binding.textSendingStatus.text = if (sending) getString(R.string.label_sending)
                            else getString(R.string.label_sending_inactive)
                    }
                }

                launch {
                    WebhookStatusStore.history.collectLatest { list ->
                        // show latest first
                        adapter.submitList(list.reversed())
                        // show last result summary when not sending
                        val last = list.lastOrNull()
                        if (last != null && WebhookStatusStore.sending.value == false) {
                            val txt = if (last.success) getString(R.string.webhook_send_success, last.sentCount, timeFormat.format(last.timestamp))
                            else getString(R.string.webhook_send_fail, (last.error ?: "Erro"), timeFormat.format(last.timestamp))
                            binding.textSendingStatus.text = txt
                        }
                    }
                }
            }
        }

        // Initialize toggle text according to service state
        binding.btnToggleWebhook.text = if (WebhookService.isRunning) getString(R.string.btn_stop_webhook) else getString(R.string.btn_start_webhook)
        binding.textWebhookStatus.text = if (WebhookService.isRunning) "Webhook ativo: Sim" else "Webhook ativo: Não"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
