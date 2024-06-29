package com.example.madcamp_task1.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [Event::class, Image::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun eventDao() : EventDao
    abstract fun imageDao() : ImageDao

    companion object{
        @Volatile
        private var instance : AppDatabase ?= null


        fun getInstance(context : Context) : AppDatabase? {
            if(instance == null){
                synchronized(AppDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "event_database"
                    ).build()
                }
            }
            return instance
        }
    }
}