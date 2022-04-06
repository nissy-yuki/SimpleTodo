package com.nisilab.simpletodo.di.database

import androidx.room.*

@Dao
interface TodoDao {
    @Query("SELECT * FROM todo_table")
    suspend fun loadAllItems(): List<TodoItem>

    @Insert
    suspend fun addItem(item: TodoItem)

    @Update
    suspend fun updateItem(item: TodoItem)

    @Delete
    suspend fun deleteItem(item: TodoItem)


}