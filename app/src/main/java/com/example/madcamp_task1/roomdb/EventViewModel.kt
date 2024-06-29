package com.example.madcamp_task1.roomdb

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class EventViewModel(private val eventDao: EventDao) : ViewModel() {

    val getAllEvents: LiveData<List<Event>> = eventDao.getAllEvents()

    fun addEvent(event: Event) {
        viewModelScope.launch {
            eventDao.addEvent(event)
        }
    }
}