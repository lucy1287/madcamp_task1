package com.example.madcamp_task1.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "profiles")
data class Profile(
    @PrimaryKey val phonenum: String,
    val name: String,
    var groupname: String = "없음",
    @TypeConverters(Converters::class)
    var skills: ArrayList<Float> // JSON
)