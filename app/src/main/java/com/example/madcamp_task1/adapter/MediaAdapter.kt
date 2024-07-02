package com.example.madcamp_task1.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_task1.R
import com.example.madcamp_task1.roomdb.Media
import com.example.madcamp_task1.roomdb.MediaType

class MediaAdapter(private val mediaList: List<Media>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_IMAGE = 1
        private const val VIEW_TYPE_VIDEO = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (mediaList[position].type) {
            MediaType.IMAGE -> VIEW_TYPE_IMAGE
            MediaType.VIDEO -> VIEW_TYPE_VIDEO
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_IMAGE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
                ImageViewHolder(view)
            }
            VIEW_TYPE_VIDEO -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
                VideoViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val media = mediaList[position]
        when (holder.itemViewType) {
            VIEW_TYPE_IMAGE -> (holder as ImageViewHolder).bind(media)
            VIEW_TYPE_VIDEO -> (holder as VideoViewHolder).bind(media)
        }
    }

    override fun getItemCount(): Int = mediaList.size

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)

        fun bind(media: Media) {
            imageView.setImageURI(media.uri)
        }
    }

    class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val videoView: VideoView = itemView.findViewById(R.id.videoView)

        fun bind(media: Media) {
            videoView.setVideoURI(media.uri)
            videoView.start() // 필요한 경우, 비디오를 자동 재생하도록 설정
        }
    }
}