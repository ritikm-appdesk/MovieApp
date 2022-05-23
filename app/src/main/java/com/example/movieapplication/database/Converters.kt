package com.example.movieapplication.database

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromSource(genre_ids:List<Int>):String{
        return genre_ids[1].toString()
    }
    @TypeConverter
    fun toSource(int :String): List<Int> {
        return listOf(int.toInt())
    }
}