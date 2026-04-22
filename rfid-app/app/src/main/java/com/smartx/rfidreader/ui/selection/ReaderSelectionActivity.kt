package com.smartx.rfidreader.ui.selection

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.smartx.rfidreader.databinding.ActivitySelectionBinding
import com.smartx.rfidreader.ui.main.MainActivity
import kotlinx.coroutines.launch

class ReaderSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectionBinding
    private val viewModel: ReaderSelectionViewModel by viewModels()
    private lateinit var adapter: ReaderListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

                    binding.progressBar.visibility =
                        if (state.isConnecting) View.VISIBLE else View.GONE

                    state.errorMessage?.let { msg ->
                        Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG).show()
                        viewModel.clearError()
                    }

                    state.connectedReader?.let { reader ->
                        val intent = Intent(this@ReaderSelectionActivity, MainActivity::class.java)
                        intent.putExtra(MainActivity.EXTRA_READER_ID, reader.readerId)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }
}
