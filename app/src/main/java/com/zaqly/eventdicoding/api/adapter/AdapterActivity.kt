package com.zaqly.eventdicoding.api.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.zaqly.eventdicoding.R
import com.zaqly.eventdicoding.api.model.ListEventsItem

class AdapterActivity (
    private var events: List<ListEventsItem>,
    private val onItemClick: (String) -> Unit
    ) : RecyclerView.Adapter<AdapterActivity.EventViewHolder>() {

    class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleEvent: TextView = itemView.findViewById(R.id.title_event)
        private val imgEvent: ImageView = itemView.findViewById(R.id.img_event)

        fun bind(event: ListEventsItem) {
            titleEvent.text = event.name
            Glide.with(itemView.context)
                .load(event.mediaCover)
                .into(imgEvent)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_main, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = events[position]
        holder.bind(event)

        holder.itemView.setOnClickListener {
            event.id?.let { eventId ->
                onItemClick(eventId.toString())
            } ?: run {
                onItemClick("")
            }
        }
    }

    override fun getItemCount(): Int = events.size

    fun updateEvents(newEvents: List<ListEventsItem>) {
        events = newEvents
        notifyDataSetChanged()
    }

}