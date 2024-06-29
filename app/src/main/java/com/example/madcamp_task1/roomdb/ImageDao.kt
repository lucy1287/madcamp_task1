package com.example.madcamp_task1.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addImage(image: Image)
}