package com.example.madcamp_task1.roomdb

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image_table")
data class Image(
    @PrimaryKey(autoGenerate = true)
    val imageNo: Long = 0,
    val uri: Uri,
    val eventNo: Long,
    val longitude: Double = 0.0,
    val latitude: Double = 0.0
)
