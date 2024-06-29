package com.example.madcamp_task1.repository

import androidx.lifecycle.LiveData
import com.example.madcamp_task1.data.Profile
import com.example.madcamp_task1.data.ProfileDao

class ProfileRepository(private val profileDao: ProfileDao) {
    val allProfiles: LiveData<List<Profile>> = profileDao.getAllProfiles()

    suspend fun insertProfile(profile: Profile) {
        profileDao.insertProfile(profile)
    }

    suspend fun updateProfile(profile: Profile) {
        profileDao.updateProfile(profile)
    }

    suspend fun deleteProfile(profile: Profile) {
        profileDao.deleteProfile(profile)
    }
}
