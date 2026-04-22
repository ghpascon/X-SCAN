package com.smartx.rfidreader.ui.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.tabs.TabLayoutMediator
import com.smartx.rfidreader.R
import com.smartx.rfidreader.databinding.ActivityMainBinding
import com.smartx.rfidreader.ui.main.config.ConfigFragment
import com.smartx.rfidreader.ui.main.reading.ReadingFragment
import kotlinx.coroutines.launch

/**
 * Activity principal — carrega o leitor selecionado e exibe as duas abas:
 *  Tab 0: Configurações do leitor RFID
 *  Tab 1: Leitura de tags
 */
class MainActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_READER_ID = "extra_reader_id"

        /** Keycodes comuns de gatilhos físicos (KeyEvent padrão) */
        private val TRIGGER_KEYCODES = intArrayOf(
            KeyEvent.KEYCODE_F1,
            KeyEvent.KEYCODE_FOCUS,
            293,  // C72 scan key
            KeyEvent.KEYCODE_HEADSETHOOK,
            KeyEvent.KEYCODE_BUTTON_R1
        )

        /** AT907: trigger via BroadcastReceiver — keycodes 133 (esq), 134 (gatilho), 135 (dir) */
        private val AT907_TRIGGER_KEYCODES = intArrayOf(133, 134, 135)
    }

    private lateinit var binding: ActivityMainBinding
    val viewModel: MainViewModel by viewModels()

    /** BroadcastReceiver para o gatilho físico do AT907 */
    private val at907TriggerReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val keyCode = intent.getIntExtra("keyCode", 0)
                .let { if (it == 0) intent.getIntExtra("keycode", 0) else it }
            val isKeyDown = intent.getBooleanExtra("keydown", false)
            if (keyCode in AT907_TRIGGER_KEYCODES) {
                if (isKeyDown) viewModel.onTriggerPressed()
                else viewModel.onTriggerReleased()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val readerId = intent.getStringExtra(EXTRA_READER_ID) ?: run {
            finish()
            return
        }

        viewModel.init(readerId)
        setupTabs()
        observeConnectionState()
    }

    private fun setupTabs() {
        val adapter = MainPagerAdapter(this)
        binding.viewPager.adapter = adapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.tab_config)
                1 -> getString(R.string.tab_reading)
                else -> ""
            }
        }.attach()
    }

    private fun observeConnectionState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    supportActionBar?.subtitle = when (state.connectionState) {
                        com.smartx.rfidreader.core.reader.ReaderConnectionState.CONNECTED ->
                            getString(R.string.status_connected, viewModel.reader?.displayName ?: "")
                        com.smartx.rfidreader.core.reader.ReaderConnectionState.CONNECTING ->
                            getString(R.string.status_connecting)
                        else -> getString(R.string.status_disconnected)
                    }
                }
            }
        }
    }

    /** Intercepta teclas do gatilho físico do leitor */
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.keyCode in TRIGGER_KEYCODES) {
            return when (event.action) {
                KeyEvent.ACTION_DOWN -> {
                    viewModel.onTriggerPressed()
                    true
                }
                KeyEvent.ACTION_UP -> {
                    viewModel.onTriggerReleased()
                    true
                }
                else -> super.dispatchKeyEvent(event)
            }
        }
        return super.dispatchKeyEvent(event)
    }

    override fun onDestroy() {
        super.onDestroy()
        // O ViewModel lida com a desconexão no onCleared()
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter().apply {
            addAction("android.rfid.FUN_KEY")
            addAction("android.intent.action.FUN_KEY")
        }
        registerReceiver(at907TriggerReceiver, filter)
    }

    override fun onPause() {
        super.onPause()
        try { unregisterReceiver(at907TriggerReceiver) } catch (_: Exception) {}
    }
}
