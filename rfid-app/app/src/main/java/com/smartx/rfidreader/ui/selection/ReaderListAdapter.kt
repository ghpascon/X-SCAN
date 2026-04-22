package com.smartx.rfidreader.ui.selection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.smartx.rfidreader.core.reader.IRfidReader
import com.smartx.rfidreader.databinding.ItemReaderBinding

class ReaderListAdapter(
    private val onConnect: (IRfidReader) -> Unit
) : ListAdapter<IRfidReader, ReaderListAdapter.ViewHolder>(DiffCallback()) {

    var connectedReaderId: String = ""
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(private val binding: ItemReaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(reader: IRfidReader) {
            val isConnected = reader.readerId == connectedReaderId
            binding.textReaderName.text = reader.displayName
            binding.textReaderId.text = reader.readerId
            binding.chipBle.visibility = if (reader.isBle) android.view.View.VISIBLE else android.view.View.GONE
            binding.btnConnect.text = if (isConnected)
                binding.root.context.getString(com.smartx.rfidreader.R.string.status_connected_short)
            else
                binding.root.context.getString(com.smartx.rfidreader.R.string.btn_connect)
            binding.btnConnect.isEnabled = !isConnected
            binding.btnConnect.setOnClickListener { onConnect(reader) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemReaderBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<IRfidReader>() {
        override fun areItemsTheSame(old: IRfidReader, new: IRfidReader) = old.readerId == new.readerId
        override fun areContentsTheSame(old: IRfidReader, new: IRfidReader) = old.readerId == new.readerId
    }
}
