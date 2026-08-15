package com.smartx.rfidreader.ui.main.webhook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smartx.rfidreader.R
import com.smartx.rfidreader.core.webhook.WebhookSendStatus
import java.text.SimpleDateFormat
import java.util.Locale
import androidx.core.content.ContextCompat

class WebhookStatusAdapter : RecyclerView.Adapter<WebhookStatusAdapter.VH>() {

    private val items = ArrayList<WebhookSendStatus>()
    private val timeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    fun submitList(list: List<WebhookSendStatus>) {
        items.clear()
        items.addAll(list.reversed()) // show newest first
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_webhook_status, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.time.text = timeFormat.format(item.timestamp)
        val ctx = holder.itemView.context
        if (item.success) {
            holder.detail.setTextColor(ContextCompat.getColor(ctx, android.R.color.holo_green_dark))
            holder.detail.text = "Sucesso — ${item.sentCount} tag(s)"
        } else {
            holder.detail.setTextColor(ContextCompat.getColor(ctx, android.R.color.holo_red_dark))
            holder.detail.text = "Falha — ${item.error ?: "Erro desconhecido"}"
        }
    }

    override fun getItemCount(): Int = items.size

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val time: TextView = v.findViewById(R.id.textStatusTime)
        val detail: TextView = v.findViewById(R.id.textStatusDetail)
    }
}
