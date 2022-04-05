package com.nisilab.simpletodo.di.repository

import com.nisilab.simpletodo.di.database.TodoDao
import com.nisilab.simpletodo.di.database.TodoDatabase
import com.nisilab.simpletodo.di.database.TodoItem
import javax.inject.Inject

class DataRepositoryImpl @Inject constructor(): DataRepository {
    @Inject lateinit var dao: TodoDao

    override suspend fun getItems(): List<TodoItem> {
        return dao.loadAllItems()
    }

    override suspend fun addItem(item: TodoItem) {
        dao.addItem(item)
    }

    override suspend fun updateItem(item: TodoItem) {
        dao.updateItem(item)
    }

    override suspend fun deleteItem(item: TodoItem) {
        dao.deleteItem(item)
    }

}