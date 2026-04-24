package com.smartx.rfidreader.ui.main.radar

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.smartx.rfidreader.R
import com.smartx.rfidreader.core.reader.ReaderConnectionState
import com.smartx.rfidreader.databinding.FragmentRadarBinding
import com.smartx.rfidreader.ui.main.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.sin

class RadarFragment : Fragment() {

    private var _binding: FragmentRadarBinding? = null
    private val binding get() = _binding!!

    private val mainViewModel: MainViewModel by activityViewModels()
    private val radarViewModel: RadarViewModel by viewModels()

    private lateinit var targetAdapter: RadarTargetAdapter

    // ---- áudio de proximidade (tom senoidal contínuo) ----
    private var audioTrack: AudioTrack? = null
    private var audioJob: Job? = null
    /** Frequência alvo em Hz; 0 = silenciar. @Volatile para acesso de thread IO. */
    @Volatile private var targetFreq = 0f

    private var wasInventorying = false

    companion object {
        private const val SAMPLE_RATE = 8000          // Hz — suficiente para beeps
        private const val CHUNK = SAMPLE_RATE / 20    // 50 ms por escrita
    }

    // -------------------------------------------------------------------------
    // Lifecycle
    // -------------------------------------------------------------------------

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRadarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupPowerSlider()
        setupAddTarget()
        setupButtons()
        observeState()
    }

    override fun onStart() {
        super.onStart()
        mainViewModel.enterRadarMode()
        startAudio()
        radarViewModel.startBuzzerLoop()
    }

    override fun onStop() {
        super.onStop()
        if (mainViewModel.reader?.isInventorying() == true) {
            mainViewModel.toggleInventory()
        }
        radarViewModel.stopBuzzerLoop()
        stopAudio()
        mainViewModel.exitRadarMode()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // -------------------------------------------------------------------------
    // Setup
    // -------------------------------------------------------------------------

    private fun setupRecyclerView() {
        targetAdapter = RadarTargetAdapter(onRemove = { radarViewModel.removeTarget(it) })
        binding.recyclerViewTargets.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewTargets.adapter = targetAdapter
    }

    private fun setupPowerSlider() {
        // Inicializa slider com a configuração atual do leitor
        val currentPower = mainViewModel.uiState.value.config.txPower.toFloat()
        binding.sliderPower.value = currentPower.coerceIn(5f, 33f)
        binding.textPowerValue.text = "${binding.sliderPower.value.toInt()} dBm"

        binding.sliderPower.addOnChangeListener { _, value, _ ->
            binding.textPowerValue.text = "${value.toInt()} dBm"
        }

        // Aplica potência ao soltar o slider
        binding.sliderPower.addOnSliderTouchListener(object :
            com.google.android.material.slider.Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: com.google.android.material.slider.Slider) {}
            override fun onStopTrackingTouch(slider: com.google.android.material.slider.Slider) {
                val newPower = slider.value.toInt()
                val current = mainViewModel.uiState.value.config
                // Forçar session=0 quando ajustar potência no modo Radar
                mainViewModel.saveConfig(current.copy(txPower = newPower, session = 0))
            }
        })
    }

    private fun setupAddTarget() {
        binding.editEpc.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                addManualTarget()
                true
            } else false
        }
        binding.btnAddTarget.setOnClickListener { addManualTarget() }

        binding.btnScanTargets.setOnClickListener {
            val existing = radarViewModel.targets.value.map { it.epc }.toSet()
            RadarScanSelectDialog(
                existingEpcs = existing,
                onTagsSelected = { epcs -> radarViewModel.addTargets(epcs) }
            ).show(childFragmentManager, "RadarScanSelectDialog")
        }
    }

    private fun setupButtons() {
        binding.btnToggleRadar.setOnClickListener {
            mainViewModel.toggleInventory()
        }
        binding.btnClearTargets.setOnClickListener {
            radarViewModel.clearTargets()
        }
    }

    private fun addManualTarget() {
        val epc = binding.editEpc.text?.toString().orEmpty().trim()
        if (epc.isBlank()) return
        val added = radarViewModel.addTarget(epc)
        if (added) {
            binding.editEpc.text?.clear()
            hideKeyboard()
        } else {
            Snackbar.make(binding.root, "EPC já adicionado ou inválido", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun hideKeyboard() {
        requireContext().getSystemService<InputMethodManager>()
            ?.hideSoftInputFromWindow(binding.editEpc.windowToken, 0)
    }

    // -------------------------------------------------------------------------
    // Observação de estado
    // -------------------------------------------------------------------------

    private fun observeState() {
        viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Sincroniza com o estado atual para evitar onScanStarted espúrio ao retomar
                wasInventorying = mainViewModel.uiState.value.isInventorying

                // Estado da conexão / inventário
                launch {
                    mainViewModel.uiState.collect { state ->
                        val connected = state.connectionState == ReaderConnectionState.CONNECTED
                        val inventorying = state.isInventorying

                        // Limpa badges ao iniciar nova sessão de leitura
                        if (inventorying && !wasInventorying) {
                            radarViewModel.onScanStarted()
                        }
                        wasInventorying = inventorying

                        binding.btnToggleRadar.text =
                            if (inventorying) "Parar Radar" else "Iniciar Radar"
                        binding.btnToggleRadar.isEnabled = connected

                        // Sincroniza slider quando config muda externamente
                        val power = state.config.txPower.toFloat().coerceIn(5f, 33f)
                        if (binding.sliderPower.value != power) {
                            binding.sliderPower.value = power
                            binding.textPowerValue.text = "${power.toInt()} dBm"
                        }
                    }
                }

                // Leituras brutas (SharedFlow seguro para múltiplos collectors) → atualiza RSSI dos targets
                launch {
                    mainViewModel.tagEventFlow.collect { tag ->
                        radarViewModel.onSingleTagRead(tag)
                    }
                }

                // Lista de targets
                launch {
                    radarViewModel.targets.collect { targets ->
                        targetAdapter.submitList(targets.toList())
                        binding.textNoTargets.visibility =
                            if (targets.isEmpty()) View.VISIBLE else View.GONE
                        binding.recyclerViewTargets.visibility =
                            if (targets.isEmpty()) View.GONE else View.VISIBLE

                        // Contador detectadas/total acima da lista
                        val found = targets.count { it.isVisible }
                        binding.textFoundCount.visibility =
                            if (targets.isEmpty()) View.GONE else View.VISIBLE
                        binding.textFoundCount.text = "$found/${targets.size} detectadas"
                    }
                }

                // Tom de proximidade contínuo: atualiza frequência conforme RSSI
                launch {
                    radarViewModel.activeBestRssi.collect { rssi ->
                        targetFreq = if (rssi != null) rssiToFrequency(rssi) else 0f
                    }
                }
            }
        }
    }

    // -------------------------------------------------------------------------
    // Áudio de proximidade
    // -------------------------------------------------------------------------

    /**
     * Mapeia RSSI para frequência:
     * -90 dBm (longe) → 300 Hz  |  -30 dBm (perto) → 1200 Hz
     */
    private fun rssiToFrequency(rssi: Double): Float {
        val t = (rssi.coerceIn(-90.0, -30.0) + 90.0) / 60.0 // 0 (longe) → 1 (perto)
        return (300.0 + t * 900.0).toFloat()
    }

    /**
     * Cria o AudioTrack e inicia o loop de escrita de onda senoidal em background.
     * O loop lê [targetFreq] a cada chunk (50 ms); freq=0 gera silêncio.
     */
    private fun startAudio() {
        val minBuf = try {
            AudioTrack.getMinBufferSize(
                SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT
            )
        } catch (_: Exception) { return }

        audioTrack = try {
            @Suppress("DEPRECATION")
            AudioTrack(
                AudioManager.STREAM_MUSIC, SAMPLE_RATE,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
                maxOf(minBuf, CHUNK * 4), AudioTrack.MODE_STREAM
            ).also { it.play() }
        } catch (_: Exception) { null } ?: return

        val buffer = ShortArray(CHUNK)
        audioJob = lifecycleScope.launch(Dispatchers.IO) {
            var phase = 0.0
            while (isActive) {
                val freq = targetFreq
                if (freq > 0f) {
                    val inc = 2.0 * PI * freq / SAMPLE_RATE
                    for (i in buffer.indices) {
                        phase += inc
                        if (phase >= 2.0 * PI) phase -= 2.0 * PI
                        // amplitude 50% para não saturar no alto-falante
                        buffer[i] = (sin(phase) * 16000.0).toInt().toShort()
                    }
                } else {
                    buffer.fill(0)
                    phase = 0.0
                }
                audioTrack?.write(buffer, 0, buffer.size)
            }
        }
    }

    private fun stopAudio() {
        targetFreq = 0f
        audioJob?.cancel()
        audioJob = null
        audioTrack?.pause()
        audioTrack?.flush()
        audioTrack?.release()
        audioTrack = null
    }
}
