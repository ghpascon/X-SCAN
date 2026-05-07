package com.smartx.rfidreader.ui.base

import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewbinding.ViewBinding
import com.smartx.rfidreader.R
import com.smartx.rfidreader.core.reader.ActiveReaderState
import com.smartx.rfidreader.core.reader.ReaderConnectionState
import com.smartx.rfidreader.databinding.ActivityBaseBinding
import com.smartx.rfidreader.databinding.HeaderAppBinding
import kotlinx.coroutines.launch

/**
 * Activity base que fornece automaticamente:
 *  - header_app (nome do leitor + status de conexão) em TODAS as telas
 *  - footer_app (device ID) em TODAS as telas
 *
 * O header é atualizado automaticamente via [ActiveReaderState] — não é
 * necessário chamar nada nas subclasses. Qualquer nova Activity que estenda
 * BaseActivity exibirá o estado real do leitor sem código adicional.
 *
 * Como usar ao criar uma nova Activity:
 *  1. Estenda `BaseActivity<SuaActivityBinding>()`
 *  2. Implemente `inflateBinding` retornando `SuaActivityBinding.inflate(inflater)`
 *  3. Implemente `onActivityReady` com o código que antes ficava em `onCreate`
 *  4. Use `binding` para acessar as views do seu layout
 *  5. Use `headerBinding.headerLogo` etc. para acesso direto ao header se necessário
 */
abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    private lateinit var _baseBinding: ActivityBaseBinding

    /** Binding do conteúdo específico da tela — disponível em onActivityReady */
    protected lateinit var binding: VB

    /** Acesso direto ao header quando necessário além da atualização automática */
    protected val headerBinding: HeaderAppBinding
        get() = _baseBinding.headerApp

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _baseBinding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(_baseBinding.root)

        // Infla o layout da subclasse e insere no slot central
        binding = inflateBinding(layoutInflater)
        _baseBinding.baseContent.addView(
            binding.root,
            FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
        )

        // Device ID no footer — leitura única, sem ViewModel
        val deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
            ?: "unknown"
        _baseBinding.footerApp.footerDeviceId.text = "ID: $deviceId"

        // Observa o estado global do leitor — atualiza header em TODAS as telas automaticamente
        observeReaderState()

        onActivityReady(savedInstanceState)
    }

    private fun observeReaderState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    ActiveReaderState.readerName.collect { name ->
                        _baseBinding.headerApp.headerReaderName.text = name
                    }
                }
                launch {
                    kotlinx.coroutines.flow.combine(
                        ActiveReaderState.connectionState,
                        ActiveReaderState.isInventorying
                    ) { state, inventorying -> state to inventorying }
                        .collect { (state, inventorying) ->
                            val statusText = when {
                                inventorying -> "Lendo"
                                state == ReaderConnectionState.CONNECTING -> "Conectando..."
                                state == ReaderConnectionState.CONNECTED -> "Conectado"
                                state == ReaderConnectionState.ERROR -> "Erro"
                                else -> "Desconectado"
                            }
                            val statusDot = when {
                                inventorying -> R.drawable.ic_status_active
                                state == ReaderConnectionState.CONNECTED -> R.drawable.ic_status_connected
                                state == ReaderConnectionState.CONNECTING -> R.drawable.ic_status_connected
                                else -> R.drawable.ic_status_disconnected
                            }
                            _baseBinding.headerApp.headerConnectionStatus.text = statusText
                            _baseBinding.headerApp.headerStatusDot.setBackgroundResource(statusDot)
                        }
                }
            }
        }
    }

    /**
     * Retorna o ViewBinding do layout específico desta tela.
     * Ex: `override fun inflateBinding(inflater: LayoutInflater) = ActivitySyncBinding.inflate(inflater)`
     */
    protected abstract fun inflateBinding(inflater: LayoutInflater): VB

    /**
     * Substitui onCreate — o binding já está disponível quando este método é chamado.
     */
    protected open fun onActivityReady(savedInstanceState: Bundle?) {}

    /**
     * Sobrescreve o header manualmente para casos especiais (ex: forçar texto fixo).
     * Na maioria dos casos não é necessário — o header atualiza sozinho via ActiveReaderState.
     */
    protected fun updateHeader(
        readerName: String,
        statusText: String,
        @DrawableRes statusDotRes: Int
    ) {
        _baseBinding.headerApp.headerReaderName.text = readerName
        _baseBinding.headerApp.headerConnectionStatus.text = statusText
        _baseBinding.headerApp.headerStatusDot.setBackgroundResource(statusDotRes)
    }
}

