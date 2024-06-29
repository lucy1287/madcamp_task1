package com.example.madcamp_task1.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_task1.GalleryActivity
import com.example.madcamp_task1.roomdb.Event
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
        val event = events[position]
        holder.bind(event)

        holder.itemView.setOnClickListener{
            val clickedEventNo = event.eventNo
            val intent = Intent(holder.itemView?.context, GalleryActivity::class.java)
            intent.putExtra("eventNo", clickedEventNo)
            ContextCompat.startActivity(holder.itemView.context, intent, null)
        }
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