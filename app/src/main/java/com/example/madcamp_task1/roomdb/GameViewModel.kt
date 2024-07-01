package com.example.madcamp_task1.roomdb

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val gameDao = AppDatabase.getInstance(application).gameDao()

    fun insert(game: Game) {
        viewModelScope.launch(Dispatchers.IO) {
            gameDao.insert(game)
        }
    }

    fun getGameByDate(selectedDate: String): LiveData<Game> {
        return gameDao.getGameByDate(selectedDate)
    }
}