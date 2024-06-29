package com.example.madcamp_task1.roomdb

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProfileDao {
    @Query("SELECT * FROM profiles ORDER BY name ASC")
    fun getAllProfiles(): LiveData<List<Profile>>

    @Query("SELECT * FROM profiles WHERE phonenum = :phonenum")
    fun getProfileByPhoneNum(phonenum: String): LiveData<Profile>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(profile: Profile)

    @Update
    suspend fun update(profile: Profile)

//    @Delete
//    suspend fun deleteProfile(profile: Profile)
}
