package com.smartx.rfidreader.ui.main.reader

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smartx.rfidreader.databinding.ItemBleDeviceBinding

class BleDeviceAdapter(
    private val onSelect: (name: String, address: String) -> Unit
) : RecyclerView.Adapter<BleDeviceAdapter.ViewHolder>() {

    private val devices = mutableListOf<Pair<String, String>>() // name, address

    fun addDevice(name: String, address: String) {
        if (devices.none { it.second == address }) {
            devices.add(Pair(name, address))
            notifyItemInserted(devices.size - 1)
        }
    }

    fun clear() {
        val size = devices.size
        devices.clear()
        notifyItemRangeRemoved(0, size)
    }

    inner class ViewHolder(private val binding: ItemBleDeviceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(name: String, address: String) {
            binding.textBleName.text = name
            binding.textBleAddress.text = address
            binding.root.setOnClickListener { onSelect(name, address) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemBleDeviceBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount() = devices.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (name, address) = devices[position]
        holder.bind(name, address)
    }
}
