package com.nisilab.simpletodo.di.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [TodoItem::class], version = 1,exportSchema = false)
@TypeConverters(DateTimeConverter::class)
abstract class TodoDatabase: RoomDatabase() {
    abstract fun todoDao(): TodoDao
}