package com.nisilab.simpletodo.di.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nisilab.simpletodo.di.database.TodoItem
import com.nisilab.simpletodo.di.repository.DataRepository
import com.nisilab.simpletodo.recycle.RecycleItem
import kotlinx.coroutines.launch
import javax.inject.Inject


class ListViewModel @ViewModelInject constructor(
    private val repository: DataRepository
): ViewModel() {
    private val _todoItems: MutableLiveData<List<TodoItem>> = MutableLiveData()
    private val _recycleItems: MutableLiveData<List<RecycleItem>> = MutableLiveData()
    private val _outRecycleItems: MutableLiveData<List<RecycleItem>> = MutableLiveData()


    val todoItems: LiveData<List<TodoItem>> = _todoItems
    // 全リサイクルアイテム
    val recycleItems: LiveData<List<RecycleItem>> = _recycleItems
    // 出力用リサイクルアイテム
    val outRecycleItems: LiveData<List<RecycleItem>> = _outRecycleItems


    fun setAllItems(){
        viewModelScope.launch {
            _todoItems.value = repository.getItems()
            if(!_todoItems.value.isNullOrEmpty()) _todoItems.value!!.sortedWith( compareBy<TodoItem> {it.deadLine} )
            setRecycleItems()
        }
    }

    fun setOutItems(){
        _outRecycleItems.value = _recycleItems.value
    }

    // 完了ボタンを押された時
    fun updateItem(item: TodoItem){
        viewModelScope.launch {
            item.isFinish = !item.isFinish
            repository.updateItem(item)
        }
    }

    // 削除ボタンを押された時
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
        setOutItems()
    }

    fun setOpenFlag(item: RecycleItem){
        val list = _recycleItems.value!!
        list.find { it.id == item.id }!!.isOpen = true
        _recycleItems.value = list
    }

    fun setCloseFlag(item: RecycleItem){
        val list = _recycleItems.value!!
        list.find { it.id == item.id }!!.isOpen = false
        _recycleItems.value = list
    }
}