package com.example.madcamp_task1.roomdb

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ImageViewModel(private val imageDao: ImageDao, private val eventNo: Long) : ViewModel() {

    val getImagesByEventNo: LiveData<List<Image>> = imageDao.getImagesByEventNo(eventNo)

    fun addImage(image: Image) {
        viewModelScope.launch {
            imageDao.addImage(image)
        }
    }
}