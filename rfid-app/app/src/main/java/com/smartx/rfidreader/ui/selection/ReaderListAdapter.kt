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

    inner class ViewHolder(private val binding: ItemReaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(reader: IRfidReader) {
            binding.textReaderName.text = reader.displayName
            binding.textReaderId.text = reader.readerId
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
