package com.example.madcamp_task1.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profiles")
data class Profile(
    @PrimaryKey val phonenum: String,
    val name: String,
    var groupname: String = "None"
)