package com.smartx.rfidreader.ui.main.reading

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.smartx.rfidreader.core.reader.RfidTag
import com.smartx.rfidreader.databinding.ItemTagBinding
import java.text.SimpleDateFormat
import java.util.Locale

class TagListAdapter : ListAdapter<RfidTag, TagListAdapter.ViewHolder>(DiffCallback()) {

    private val timeFormat = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())

    inner class ViewHolder(private val binding: ItemTagBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tag: RfidTag) {
            binding.textEpc.text = tag.epc
            binding.textRssi.text = if (tag.rssi.isNotEmpty()) "RSSI: ${tag.rssi} dBm" else ""
            binding.textCount.text = "×${tag.readCount}"
            binding.textTime.text = timeFormat.format(tag.timestamp)
            if (tag.tid.isNotEmpty()) {
                binding.textTid.text = "TID: ${tag.tid}"
                binding.textTid.visibility = android.view.View.VISIBLE
            } else {
                binding.textTid.visibility = android.view.View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    class DiffCallback : DiffUtil.ItemCallback<RfidTag>() {
        override fun areItemsTheSame(old: RfidTag, new: RfidTag) = old.epc == new.epc
        override fun areContentsTheSame(old: RfidTag, new: RfidTag) =
            old.readCount == new.readCount && old.rssi == new.rssi
    }
}
