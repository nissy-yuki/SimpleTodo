package com.nisilab.simpletodo.di.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nisilab.simpletodo.di.database.TodoItem
import com.nisilab.simpletodo.di.repository.DataRepository
import com.nisilab.simpletodo.recycle.RecycleItem
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListViewModel @Inject constructor(
    private val repository: DataRepository
): ViewModel() {
    private val _todoItems: MutableLiveData<List<TodoItem>> = MutableLiveData()
    private val _recycleItems: MutableLiveData<List<RecycleItem>> = MutableLiveData()

    val todoItems: LiveData<List<TodoItem>> = _todoItems
    val recycleItems: LiveData<List<RecycleItem>> = _recycleItems

    fun setAllItems(){
        viewModelScope.launch {
            _todoItems.value = repository.getItems()
            setRecycleItems()
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

    fun setRecycleItems(){
        var list: MutableList<RecycleItem> = mutableListOf()
        _todoItems.value!!.forEach {
            list.add(it.toRecycleItem())
        }
        _recycleItems.value = list
    }

    fun setOpenFlag(item: RecycleItem){
        val list = _recycleItems.value
        list!!.find { it.id == item.id }!!.isOpen = true
        _recycleItems.value = list
    }

    fun setCloseFlag(item: RecycleItem){
        val list = _recycleItems.value
        list!!.find { it.id == item.id }!!.isOpen = false
        _recycleItems.value = list
    }
}