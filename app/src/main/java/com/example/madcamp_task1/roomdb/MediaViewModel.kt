package com.example.madcamp_task1.roomdb

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MediaViewModel(private val mediaDao: MediaDao, private val eventNo: Long) : ViewModel() {

    val getMediasByEventNo: LiveData<List<Media>> = mediaDao.getMediasByEventNo(eventNo)

    fun addMedia(media: Media) {
        viewModelScope.launch {
            mediaDao.addMedia(media)
        }
    }
}