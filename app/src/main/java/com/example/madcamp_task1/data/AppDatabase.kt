package com.example.madcamp_task1.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Profile::class], version = 4, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

val MIGRATION_1_2 = object : Migration(3, 4) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // make backup table for previous schema
        database.execSQL("CREATE TABLE profiles_backup (name TEXT NOT NULL, phonenum TEXT PRIMARY KEY NOT NULL, groupname TEXT DEFAULT 'None' NOT NULL)")

        database.execSQL("INSERT INTO profiles_backup (name, phonenum, groupname) SELECT name, phonenum, groupname FROM profiles")

        // drop
        database.execSQL("DROP TABLE profiles")

        // new table
        database.execSQL("CREATE TABLE profiles (phonenum TEXT PRIMARY KEY NOT NULL, name TEXT NOT NULL, groupname TEXT DEFAULT 'None' NOT NULL)")

        database.execSQL("INSERT INTO profiles (phonenum, name, groupname) SELECT phonenum, name, groupname FROM profiles_backup")

        // delete backup table
        database.execSQL("DROP TABLE profiles_backup")
    }
}