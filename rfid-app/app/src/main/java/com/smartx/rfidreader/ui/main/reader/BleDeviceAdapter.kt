package com.smartx.rfidreader.ui.main.reader

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smartx.rfidreader.databinding.ItemBleDeviceBinding

class BleDeviceAdapter(
    private val onSelect: (name: String, address: String) -> Unit
) : RecyclerView.Adapter<BleDeviceAdapter.ViewHolder>() {

    private val devices = mutableListOf<Triple<String, String, Boolean>>() // name, address, isPaired

    fun addDevice(name: String, address: String, isPaired: Boolean = false) {
        val existing = devices.indexOfFirst { it.second == address }
        if (existing == -1) {
            devices.add(Triple(name, address, isPaired))
            notifyItemInserted(devices.size - 1)
        } else if (isPaired && !devices[existing].third) {
            // atualiza para sinalizar como pareado se ainda não estava
            devices[existing] = Triple(devices[existing].first, address, true)
            notifyItemChanged(existing)
        }
    }

    fun clear() {
        val size = devices.size
        devices.clear()
        notifyItemRangeRemoved(0, size)
    }

    inner class ViewHolder(private val binding: ItemBleDeviceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(name: String, address: String, isPaired: Boolean) {
            binding.textBleName.text = name
            binding.textBleAddress.text = if (isPaired) "$address  •  Pareado" else address
            binding.root.setOnClickListener { onSelect(name, address) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemBleDeviceBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = devices.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (name, address, isPaired) = devices[position]
        holder.bind(name, address, isPaired)
    }
}
