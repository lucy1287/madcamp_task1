package com.example.madcamp_task1.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.madcamp_task1.data.Profile
import com.example.madcamp_task1.repository.ProfileRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: ProfileRepository) : ViewModel() {
    val allProfiles: LiveData<List<Profile>> = repository.allProfiles

    fun insertProfile(profile: Profile) = viewModelScope.launch {
        repository.insertProfile(profile)
    }

    fun updateProfile(profile: Profile) = viewModelScope.launch {
        repository.updateProfile(profile)
    }

    fun deleteProfile(profile: Profile) = viewModelScope.launch {
        repository.deleteProfile(profile)
    }
}
