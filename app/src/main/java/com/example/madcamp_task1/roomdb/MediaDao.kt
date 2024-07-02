package com.example.madcamp_task1.roomdb

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MediaDao {

    @Query("SELECT * FROM media WHERE eventNo = :eventNo")
    fun getMediasByEventNo(eventNo: Long): LiveData<List<Media>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMedia(media: Media)

    @Delete
    suspend fun deleteMedia(media: Media)
}