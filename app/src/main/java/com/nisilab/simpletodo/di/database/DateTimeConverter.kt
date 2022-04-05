package com.nisilab.simpletodo.di.database

import androidx.room.TypeConverter
import java.time.LocalDateTime

class DateTimeConverter {
    @TypeConverter
    fun toLocalDateTime(value: String): LocalDateTime{
        return LocalDateTime.parse(value)
    }
    @TypeConverter
    fun fromLocalDateTime(value: LocalDateTime): String{
        return value.toString()
    }
}