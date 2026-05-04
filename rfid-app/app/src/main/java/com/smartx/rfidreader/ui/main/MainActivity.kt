package com.smartx.rfidreader.ui.main

import android.os.Bundle
import android.os.SystemClock
import android.widget.Toast
import androidx.core.content.ContextCompat
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
            KeyEvent.KEYCODE_BUTTON_R1,
            523   // XR2 handle trigger
        )
        const val TAG_HOME = "home"
    }

    private lateinit var binding: ActivityMainBinding
    val viewModel: MainViewModel by viewModels()

    private var backPressedTime = 0L

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
                    val now = SystemClock.elapsedRealtime()
                    if (now - backPressedTime < 2000L) {
                        finish()
                    } else {
                        backPressedTime = now
                        Toast.makeText(
                            this@MainActivity,
                            "Pressione voltar novamente para sair",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
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

}
