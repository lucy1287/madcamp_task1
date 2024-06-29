package com.example.madcamp_task1.roomdb

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ImageViewModelFactory(private val imageDao: ImageDao, private val eventNo: Long) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ImageViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ImageViewModel(imageDao, eventNo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}