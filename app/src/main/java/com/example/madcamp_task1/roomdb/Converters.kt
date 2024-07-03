package com.example.madcamp_task1.roomdb

import android.net.Uri
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun fromUri(uri: Uri?): String? {
        return uri?.toString()
    }

    @TypeConverter
    fun toUri(uriString: String?): Uri? {
        return uriString?.let { Uri.parse(it) }
    }

    @TypeConverter
    fun fromArrayList(value: ArrayList<String>?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toArrayList(value: String?): ArrayList<String>? {
        val listType = object : TypeToken<ArrayList<String>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(value: String): ArrayList<Float> {
        val listType = object : TypeToken<ArrayList<Float>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromFloatArrayList(list: ArrayList<Float>): String {
        return Gson().toJson(list)
    }
}