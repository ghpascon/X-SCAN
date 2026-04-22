package com.smartx.rfidreader.ui.sync

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.smartx.rfidreader.R
import com.smartx.rfidreader.databinding.ItemSyncResultBinding

class SyncProgressAdapter : ListAdapter<SyncProgressItem, SyncProgressAdapter.ViewHolder>(DIFF) {

    inner class ViewHolder(private val binding: ItemSyncResultBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SyncProgressItem) {
            binding.textSyncItemLabel.text = item.label
            when (item.status) {
                ItemStatus.WAITING -> {
                    binding.imgSyncStatus.setImageResource(R.drawable.ic_status_inactive)
                    binding.textSyncItemStatus.text = "Aguardando..."
                    binding.textSyncItemStatus.setTextColor(
                        ContextCompat.getColor(binding.root.context, android.R.color.darker_gray)
                    )
                }
                ItemStatus.SUCCESS -> {
                    binding.imgSyncStatus.setImageResource(R.drawable.ic_status_active)
                    binding.textSyncItemStatus.text = "Enviado com sucesso"
                    binding.textSyncItemStatus.setTextColor(
                        ContextCompat.getColor(binding.root.context, R.color.status_active)
                    )
                }
                ItemStatus.ERROR -> {
                    binding.imgSyncStatus.setImageResource(R.drawable.ic_sync_error)
                    binding.textSyncItemStatus.text = item.errorMessage ?: "Erro desconhecido"
                    binding.textSyncItemStatus.setTextColor(
                        ContextCompat.getColor(binding.root.context, android.R.color.holo_red_dark)
                    )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSyncResultBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<SyncProgressItem>() {
            override fun areItemsTheSame(a: SyncProgressItem, b: SyncProgressItem) =
                a.eventId == b.eventId
            override fun areContentsTheSame(a: SyncProgressItem, b: SyncProgressItem) = a == b
        }
    }
}
