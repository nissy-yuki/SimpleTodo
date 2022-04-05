package com.nisilab.simpletodo.di.repository

import com.nisilab.simpletodo.di.database.TodoItem

interface DataRepository {
    suspend fun getItems(): List<TodoItem>
    suspend fun addItem(item: TodoItem)
    suspend fun updateItem(item: TodoItem)
    suspend fun deleteItem(item: TodoItem)
}