package com.example.madcamp_task1.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.madcamp_task1.databinding.ItemImageBinding
import com.example.madcamp_task1.roomdb.Image

class ImageAdapter(private val images: List<Image>) :
    RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            ItemImageBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int = images.size

    class ImageViewHolder(private val binding : ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(image: Image) {
            binding.imageView.setImageURI(image.uri)
        }
    }
}