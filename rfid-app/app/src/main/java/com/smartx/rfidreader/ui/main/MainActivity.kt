package com.smartx.rfidreader.ui.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.smartx.rfidreader.R
import com.smartx.rfidreader.databinding.ActivityMainBinding
import com.smartx.rfidreader.core.reader.ReaderConnectionState
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    companion object {
        private val TRIGGER_KEYCODES = intArrayOf(
            KeyEvent.KEYCODE_F1,
            KeyEvent.KEYCODE_FOCUS,
            293,
            KeyEvent.KEYCODE_HEADSETHOOK,
            KeyEvent.KEYCODE_BUTTON_R1
        )
        private val AT907_TRIGGER_KEYCODES = intArrayOf(133, 134, 135)

        const val TAG_HOME = "home"
    }

    private lateinit var binding: ActivityMainBinding
    val viewModel: MainViewModel by viewModels()

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

        // Observe and populate header badge (reader + connection)
        observeHeader()

        if (savedInstanceState == null) {
            loadHome()
        }

        setupBackHandler()
        observeNavigation()
    }

    private fun observeHeader() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    val name = viewModel.reader?.displayName ?: getString(R.string.nav_reader)
                    try {
                        binding.headerApp.headerReaderName.text = name

                        val statusText = when {
                            state.isInventorying -> "Lendo"
                            state.connectionState == ReaderConnectionState.CONNECTING -> "Conectando..."
                            state.connectionState == ReaderConnectionState.CONNECTED -> "Conectado"
                            state.connectionState == ReaderConnectionState.ERROR -> "Erro"
                            else -> "Desconectado"
                        }
                        binding.headerApp.headerConnectionStatus.text = statusText

                        val statusDrawable = when {
                            state.isInventorying -> R.drawable.ic_status_active
                            state.connectionState == ReaderConnectionState.CONNECTED -> R.drawable.ic_status_connected
                            state.connectionState == ReaderConnectionState.CONNECTING -> R.drawable.ic_status_connected
                            else -> R.drawable.ic_status_disconnected
                        }
                        binding.headerApp.headerStatusDot.setBackgroundResource(statusDrawable)
                    } catch (_: Exception) {
                        // Header may not be present in some layouts; ignore
                    }
                }
            }
        }
    }

    private fun loadHome() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, HomeFragment(), TAG_HOME)
            .commit()
    }

    /** Navega para um fragmento, adicionando ao back stack */
    fun navigateTo(fragment: Fragment, tag: String? = null) {
        // Evita empilhar o mesmo fragmento
        val existing = supportFragmentManager.findFragmentByTag(tag ?: fragment::class.java.simpleName)
        if (existing != null && existing.isVisible) return

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                android.R.anim.fade_in,
                android.R.anim.fade_out,
                android.R.anim.fade_in,
                android.R.anim.fade_out
            )
            .replace(R.id.fragmentContainer, fragment, tag ?: fragment::class.java.simpleName)
            .addToBackStack(null)
            .commit()
    }

    private fun setupBackHandler() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (supportFragmentManager.backStackEntryCount > 0) {
                    supportFragmentManager.popBackStack()
                } else {
                    finish()
                }
            }
        })
    }

    private fun observeNavigation() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigateToReading.collect {
                    // Volta para a tela inicial após conectar
                    supportFragmentManager.popBackStack()
                }
            }
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.keyCode in TRIGGER_KEYCODES) {
            return when (event.action) {
                KeyEvent.ACTION_DOWN -> { viewModel.onTriggerPressed(); true }
                KeyEvent.ACTION_UP -> { viewModel.onTriggerReleased(); true }
                else -> super.dispatchKeyEvent(event)
            }
        }
        return super.dispatchKeyEvent(event)
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
