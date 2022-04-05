package com.nisilab.simpletodo.di.database

import androidx.room.*

@Dao
interface TodoDao {
    @Query("SELECT * FROM todo_table")
    fun loadAllItems(): List<TodoItem>

    @Insert
    fun addItem(item: TodoItem)

    @Update
    fun updateItem(item: TodoItem)

    @Delete
    fun deleteItem(item: TodoItem)


}