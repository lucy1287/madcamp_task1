package com.example.madcamp_task1.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_image")
data class Image(
    @PrimaryKey
    val imageNo: Int,
    val eventNo: Int,
    val longitude: Double = 0.0,
    val latitude: Double = 0.0
)
