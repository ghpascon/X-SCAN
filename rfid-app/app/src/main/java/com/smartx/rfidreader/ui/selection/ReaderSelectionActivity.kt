package com.smartx.rfidreader.ui.selection

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.smartx.rfidreader.databinding.ActivitySelectionBinding
import com.smartx.rfidreader.ui.base.BaseActivity
import com.smartx.rfidreader.ui.main.MainActivity
import kotlinx.coroutines.launch

class ReaderSelectionActivity : BaseActivity<ActivitySelectionBinding>() {

    private val viewModel: ReaderSelectionViewModel by viewModels()
    private lateinit var adapter: ReaderListAdapter

    override fun inflateBinding(inflater: LayoutInflater) = ActivitySelectionBinding.inflate(inflater)

    override fun onActivityReady(savedInstanceState: Bundle?) {
        setupRecyclerView()
        observeState()
    }

    private fun setupRecyclerView() {
        adapter = ReaderListAdapter { reader ->
            viewModel.connect(reader)
        }
        binding.recyclerViewReaders.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewReaders.adapter = adapter
    }

    private fun observeState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    adapter.submitList(state.readers)

                    val statusText = when {
                        state.isConnecting -> "Conectando..."
                        state.connectedReader != null -> "Conectado"
                        else -> "Desconectado"
                    }
                    val statusDrawable = when {
                        state.connectedReader != null -> com.smartx.rfidreader.R.drawable.ic_status_connected
                        state.isConnecting -> com.smartx.rfidreader.R.drawable.ic_status_connected
                        else -> com.smartx.rfidreader.R.drawable.ic_status_disconnected
                    }
                    updateHeader(
                        state.connectedReader?.displayName
                            ?: getString(com.smartx.rfidreader.R.string.selection_title),
                        statusText,
                        statusDrawable
                    )

                    binding.progressBar.visibility =
                        if (state.isConnecting) View.VISIBLE else View.GONE

                    state.errorMessage?.let { msg ->
                        Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG).show()
                        viewModel.clearError()
                    }

                    state.connectedReader?.let { reader ->
                        val intent = Intent(this@ReaderSelectionActivity, MainActivity::class.java)
                        intent.putExtra("reader_id", reader.readerId)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }
}
