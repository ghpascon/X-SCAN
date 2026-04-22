package com.smartx.rfidreader.ui.sync

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.smartx.rfidreader.R
import com.smartx.rfidreader.core.db.EventEntity
import com.smartx.rfidreader.databinding.ItemEventBinding
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

class EventListAdapter(
    private val onDelete: (EventEntity) -> Unit,
    private val onItemClick: (EventEntity) -> Unit = {}
) : ListAdapter<EventEntity, EventListAdapter.ViewHolder>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<EventEntity>() {
            override fun areItemsTheSame(a: EventEntity, b: EventEntity) = a.id == b.id
            override fun areContentsTheSame(a: EventEntity, b: EventEntity) = a == b
        }
    }

    inner class ViewHolder(private val binding: ItemEventBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(event: EventEntity) {
            binding.textEventDate.text = formatTimestamp(event.savedAt)
            binding.textTagCount.text = binding.root.context.resources
                .getQuantityString(R.plurals.tag_count_plural, event.tagCount, event.tagCount)
            binding.iconGps.visibility = if (event.hasGps) View.VISIBLE else View.GONE

            if (event.isSynced) {
                binding.chipStatus.text = binding.root.context.getString(R.string.event_synced)
                binding.chipStatus.setChipBackgroundColorResource(R.color.status_synced_bg)
                binding.chipStatus.setTextColor(
                    binding.root.context.getColor(R.color.status_synced_text)
                )
            } else {
                binding.chipStatus.text = binding.root.context.getString(R.string.event_pending)
                binding.chipStatus.setChipBackgroundColorResource(R.color.status_pending_bg)
                binding.chipStatus.setTextColor(
                    binding.root.context.getColor(R.color.status_pending_text)
                )
            }

            binding.btnDeleteEvent.setOnClickListener { onDelete(event) }
            binding.root.setOnClickListener { onItemClick(event) }
        }

        private fun formatTimestamp(iso: String): String {
            return try {
                val zdt = ZonedDateTime.parse(iso)
                val fmt = DateTimeFormatter
                    .ofLocalizedDateTime(FormatStyle.SHORT)
                    .withLocale(Locale("pt", "BR"))
                zdt.format(fmt)
            } catch (_: Exception) {
                iso
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}
