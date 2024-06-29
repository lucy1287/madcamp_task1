package com.example.madcamp_task1.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addImage(image: Image)

    @Query("SELECT * FROM image_table WHERE eventNo = :eventNo")
    fun getImagesByEventNo(eventNo: Long): LiveData<List<Image>>
}