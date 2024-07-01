package com.example.madcamp_task1.roomdb

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "game_table")
data class Game(
    @PrimaryKey val gameDate: String,
    val gameTitle: String,
    val gameScore: String,
    @TypeConverters(Converters::class)
    val gameMembers: ArrayList<String> // JSON
)

