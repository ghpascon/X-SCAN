package com.smartx.rfidreader.ui.main.radar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.smartx.rfidreader.core.reader.RfidTag
import com.smartx.rfidreader.databinding.ItemRadarScanTagBinding

class RadarScanAdapter(
    /** EPCs já adicionados como targets — mostrados como desabilitados */
    private val existingEpcs: Set<String> = emptySet()
) : ListAdapter<RfidTag, RadarScanAdapter.ViewHolder>(DiffCallback()) {

    private val selected = mutableSetOf<String>()

    /** Retorna os EPCs atualmente marcados pelo usuário (excluindo já existentes) */
    fun getSelectedEpcs(): List<String> = selected.filter { it !in existingEpcs }.toList()

    /** Marca todas as tags elegíveis (não existentes) como selecionadas */
    fun selectAll() {
        currentList
            .filter { it.epc.uppercase() !in existingEpcs }
            .forEach { selected.add(it.epc.uppercase()) }
        notifyItemRangeChanged(0, itemCount)
    }

    /** Desmarca todas */
    fun clearSelection() {
        selected.clear()
        notifyItemRangeChanged(0, itemCount)
    }

    inner class ViewHolder(private val binding: ItemRadarScanTagBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tag: RfidTag) {
            val epcUpper = tag.epc.uppercase()
            val alreadyAdded = epcUpper in existingEpcs

            binding.textScanEpc.text = tag.epc
            binding.textScanRssi.text = if (tag.rssi.isNotBlank()) "${tag.rssi} dBm" else ""

            if (alreadyAdded) {
                binding.checkboxSelectTag.setOnCheckedChangeListener(null)
                binding.checkboxSelectTag.isChecked = true
                binding.checkboxSelectTag.isEnabled = false
                binding.textScanRssi.text = "Já adicionada"
                binding.root.isClickable = false
                binding.root.alpha = 0.45f
            } else {
                binding.checkboxSelectTag.isEnabled = true
                binding.checkboxSelectTag.setOnCheckedChangeListener(null)
                binding.checkboxSelectTag.isChecked = selected.contains(epcUpper)
                binding.checkboxSelectTag.setOnCheckedChangeListener { _, checked ->
                    if (checked) selected.add(epcUpper)
                    else selected.remove(epcUpper)
                }
                binding.root.isClickable = true
                binding.root.alpha = 1f
                binding.root.setOnClickListener {
                    binding.checkboxSelectTag.isChecked = !binding.checkboxSelectTag.isChecked
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRadarScanTagBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    class DiffCallback : DiffUtil.ItemCallback<RfidTag>() {
        override fun areItemsTheSame(old: RfidTag, new: RfidTag) = old.epc == new.epc
        override fun areContentsTheSame(old: RfidTag, new: RfidTag) = old.rssi == new.rssi
    }
}
