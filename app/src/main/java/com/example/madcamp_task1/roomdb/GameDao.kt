package com.example.madcamp_task1.roomdb

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GameDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(game: Game)

    @Query("SELECT * FROM game_table ORDER BY gameDate DESC")
    fun getAllGames() : LiveData<List<Game>>

    @Query("SELECT gameDate, gameMembers, gameScore, gameTitle FROM game_table WHERE gameDate = :selectedDate")
    fun getGameByDate(selectedDate: String): LiveData<Game>
}
