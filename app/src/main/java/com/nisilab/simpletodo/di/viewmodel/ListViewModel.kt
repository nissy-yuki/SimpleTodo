package com.nisilab.simpletodo.di.viewmodel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nisilab.simpletodo.di.database.TodoItem
import com.nisilab.simpletodo.di.repository.DataRepository
import com.nisilab.simpletodo.recycle.RecycleItem
import kotlinx.coroutines.launch
import java.util.*


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
        }
    }

    // 完了ボタンを押された時
    fun changeItemFinishFlg(item: RecycleItem){
        item.isFinish = !item.isFinish
        updateItems(item,item.isFinish)
        viewModelScope.launch {
            repository.updateItem(item.toTodoItem())
        }
    }

    private fun updateItems(item: RecycleItem, flg: Boolean){
        var list = _todoItems.value
        list!!.find { it.id == item.id }!!.isFinish = flg
        _todoItems.value = list
    }

    // 削除ボタンを押された時
    fun deleteItem(id: Int){
        viewModelScope.launch {
            val item = searchTodoItem(id)
            _todoItems.value = _todoItems.value!!.minus(item)
            repository.deleteItem(item)
        }
    }

    // 管理用itemのセット
    fun setRecycleItems(){
        var list: MutableList<RecycleItem> = mutableListOf()
        _todoItems.value!!.forEach {
            list.add(it.toRecycleItem())
        }
        _recycleItems.value = list
    }

    // 出力用itemのセット
    fun setOutItems(){
        _outRecycleItems.value = _recycleItems.value
        // sort deadLine
        Collections.sort(_outRecycleItems.value){ v1, v2 ->
            v1.deadLine.compareTo(v2.deadLine)
        }
    }

    fun changeOpenFlag(item: RecycleItem){
        Log.d("checkValue","$item")
        val list = _recycleItems.value!!
        list.find { it.id == item.id }!!.isOpen = !item.isOpen
        _recycleItems.value = list
    }

    fun searchTodoItem(id: Int): TodoItem{
        return _todoItems.value!!.find { it.id == id }!!
    }

}