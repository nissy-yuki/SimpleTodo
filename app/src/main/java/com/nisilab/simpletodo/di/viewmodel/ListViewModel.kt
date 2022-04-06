package com.nisilab.simpletodo.di.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nisilab.simpletodo.di.database.TodoItem
import com.nisilab.simpletodo.di.repository.DataRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListViewModel @Inject constructor(
    private val repository: DataRepository
): ViewModel() {
    private val _todoItems: MutableLiveData<List<TodoItem>> = MutableLiveData()

    val todoItems: LiveData<List<TodoItem>> = _todoItems

    fun getAllItems(){
        viewModelScope.launch {
            _todoItems.value = repository.getItems()
        }
    }

    fun updateItem(item: TodoItem){
        viewModelScope.launch {
            repository.updateItem(item)
        }
    }

    fun deleteItem(item: TodoItem){
        viewModelScope.launch {
            repository.deleteItem(item)
        }
    }
}