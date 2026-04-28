package com.smartx.rfidreader.ui.sync

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.smartx.rfidreader.R
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class WebhookConfigDialogFragment : DialogFragment() {

    private val viewModel: SyncViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = LayoutInflater.from(requireContext())
        val view = inflater.inflate(R.layout.dialog_webhook_config, null)

        val edit = view.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.editDialogWebhookUrl)
        val btnExample = view.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnDialogUseExample)
        val btnSave = view.findViewById<com.google.android.material.button.MaterialButton>(R.id.btnDialogSave)

        // Pre-fill with current value
        lifecycleScope.launch {
            val current = viewModel.uiState.first()
            edit.setText(current.webhookUrl)
        }

        btnExample.setOnClickListener {
            edit.setText(getString(R.string.webhook_example_url))
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(R.string.section_webhook_config)
            .setView(view)
            .setNegativeButton(android.R.string.cancel, null)
            .create()

        btnSave.setOnClickListener {
            val url = edit.text?.toString()?.trim() ?: ""
            if (url.isNotBlank() && !url.startsWith("http")) {
                Snackbar.make(requireActivity().findViewById(android.R.id.content), getString(R.string.error_invalid_url), Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.saveWebhookUrl(url)
            Snackbar.make(requireActivity().findViewById(android.R.id.content), getString(R.string.webhook_saved), Snackbar.LENGTH_SHORT).show()
            dialog.dismiss()
        }

        return dialog
    }
}
