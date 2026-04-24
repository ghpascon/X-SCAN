package com.smartx.rfidreader.ui.main.radar

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.smartx.rfidreader.databinding.ItemRadarTargetBinding

class RadarTargetAdapter(
    private val onRemove: (epc: String) -> Unit
) : ListAdapter<RadarTarget, RadarTargetAdapter.ViewHolder>(DiffCallback()) {

    inner class ViewHolder(private val binding: ItemRadarTargetBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(target: RadarTarget) {
            binding.textTargetEpc.text = target.epc
            binding.btnRemoveTarget.setOnClickListener { onRemove(target.epc) }

            // RSSI: mostra sempre que tiver valor; verde se leitura ativa, cinza se antigo
            if (target.rssi != null) {
                binding.textTargetRssi.text = "%.0f dBm".format(target.rssi)
                binding.textTargetRssi.setTextColor(
                    if (target.isRecentlyRead) Color.parseColor("#4CAF50")
                    else Color.parseColor("#9E9E9E")
                )
            } else {
                binding.textTargetRssi.text = ""
            }

            if (target.isVisible) {
                binding.textTargetStatus.text = "Detectada"
                binding.textTargetStatus.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#4CAF50"))
            } else {
                binding.textTargetStatus.text = "Não detectada"
                binding.textTargetStatus.backgroundTintList =
                    ColorStateList.valueOf(Color.parseColor("#9E9E9E"))
            }

            val progress = (target.signalStrength * 100).toInt()
            binding.progressSignal.progress = progress
            binding.progressSignal.progressTintList = ColorStateList.valueOf(
                if (target.isRecentlyRead) signalColor(target.signalStrength)
                else Color.parseColor("#9E9E9E")
            )
        }

        /** Verde (forte) → Amarelo → Vermelho (fraco) */
        private fun signalColor(strength: Float): Int {
            val clamped = strength.coerceIn(0f, 1f)
            return when {
                clamped >= 0.6f -> Color.parseColor("#4CAF50")  // verde
                clamped >= 0.3f -> Color.parseColor("#FF9800")  // laranja
                else            -> Color.parseColor("#F44336")  // vermelho
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRadarTargetBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    class DiffCallback : DiffUtil.ItemCallback<RadarTarget>() {
        override fun areItemsTheSame(old: RadarTarget, new: RadarTarget) = old.epc == new.epc
        override fun areContentsTheSame(old: RadarTarget, new: RadarTarget) =
            old.detectedInSession == new.detectedInSession &&
            old.lastSeenMs == new.lastSeenMs &&
            old.rssi == new.rssi
    }
}
