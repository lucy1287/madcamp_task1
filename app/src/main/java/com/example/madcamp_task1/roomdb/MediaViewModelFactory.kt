package com.example.madcamp_task1.roomdb

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MediaViewModelFactory(private val mediaDao: MediaDao, private val eventNo: Long) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MediaViewModel::class.java)) {
            MediaViewModel(mediaDao, eventNo) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}