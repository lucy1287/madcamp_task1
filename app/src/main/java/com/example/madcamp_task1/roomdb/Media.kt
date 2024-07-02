package com.example.madcamp_task1.roomdb

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Media(
    @PrimaryKey(autoGenerate = true)
    val mediaNo: Long = 0,
    val uri: Uri,
    val eventNo: Long,
    val latitude: Double,
    val longitude: Double,
    val type: MediaType
)

enum class MediaType {
    IMAGE,
    VIDEO
}