package com.example.madcamp_task1.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "event_table")
data class Event(
    @PrimaryKey(autoGenerate = true)
    val eventNo: Long = 0,
    val title: String,
    val detail: String,
    val imageUrl: String,
    val createdDate: String
)
