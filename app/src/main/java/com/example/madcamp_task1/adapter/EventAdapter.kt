package com.example.madcamp_task1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_task1.data.Event
import com.example.madcamp_task1.databinding.ItemEventBinding

class EventAdapter(private val events: List<Event>) :
    RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(
            ItemEventBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        )
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        holder.bind(events[position])
    }

    override fun getItemCount(): Int = events.size

    class EventViewHolder(private val binding : ItemEventBinding) :
        RecyclerView.ViewHolder(binding.root){

        fun bind(event: Event){
            binding.tvEventTitle.text = event.title
            binding.tvEventDetail.text = event.detail
        }
    }
}