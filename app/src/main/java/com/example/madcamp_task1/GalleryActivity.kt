package com.example.madcamp_task1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.madcamp_task1.adapter.ImageAdapter
import com.example.madcamp_task1.databinding.ActivityGalleryBinding

class GalleryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGalleryBinding

    private val images = listOf(
        "https://cdn-icons-png.flaticon.com/512/1269/1269075.png",
        "https://cdn-icons-png.flaticon.com/512/1269/1269075.png",
        "https://cdn-icons-png.flaticon.com/512/1269/1269075.png",
        "https://cdn-icons-png.flaticon.com/512/1269/1269075.png",
    );

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        initializeViews()
        setContentView(binding.root)
    }

    private fun initializeViews(){
        binding.rvImageList.layoutManager = GridLayoutManager(this, 2)
        binding.rvImageList.adapter = ImageAdapter(images)

    }

}