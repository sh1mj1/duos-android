package com.example.duos.utils

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import org.threeten.bp.LocalDateTime

@ProvidedTypeConverter
class ThreetenLocalDateTimeConverter (private val gson: Gson){

    @TypeConverter
    fun threetenLocalDateTimeToJson(value: LocalDateTime): String{
        return gson.toJson(value)
    }

    @TypeConverter
    fun JsonToThreetenLocalDateTime(value: String): LocalDateTime{
        return gson.fromJson(value, LocalDateTime::class.java)
    }
}