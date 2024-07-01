package com.example.madcamp_task1.roomdb

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.madcamp_task1.roomdb.AppDatabase
import com.example.madcamp_task1.roomdb.Profile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val profileDao = AppDatabase.getInstance(application).profileDao()
    val allProfiles: LiveData<List<Profile>> = profileDao.getAllProfiles()

    fun insertProfile(profile: Profile) {
        viewModelScope.launch(Dispatchers.IO) {
            profileDao.insert(profile)
        }
    }

    fun updateProfile(profile: Profile) {
        viewModelScope.launch(Dispatchers.IO) {
            profileDao.update(profile)
        }
    }

    fun getProfileByPhoneNum(phoneNum: String): LiveData<Profile> {
        return profileDao.getProfileByPhoneNum(phoneNum)
    }

    fun searchDatabase(searchQuery: String): LiveData<List<Profile>> {
        return profileDao.searchDatabase(searchQuery).asLiveData()
    }
}
