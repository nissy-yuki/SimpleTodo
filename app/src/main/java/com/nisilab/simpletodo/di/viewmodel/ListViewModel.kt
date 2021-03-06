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
import java.time.LocalDateTime
import java.util.*
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
        }
    }

    // 完了ボタンを押された時
    fun updateItem(item: RecycleItem, flg: Boolean){
        viewModelScope.launch {
            updateItems(item,flg)
            item.isFinish = flg
            repository.updateItem(item.toTodoItem())
        }
    }

    private fun updateItems(item: RecycleItem, flg: Boolean){
        _todoItems.value!!.find { it.id == item.id }!!.isFinish = flg
        updateRecycleItem(item, flg)
    }

    // 削除ボタンを押された時
    fun deleteItem(id: Int){
        viewModelScope.launch {
            val item = searchItem(id)
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
        val list = _recycleItems.value
        // sort deadLine
        Collections.sort(list){ v1, v2 ->
            v1.deadLine.compareTo(v2.deadLine)
        }
        _outRecycleItems.value = list
    }

    // item の開閉
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

    fun searchItem(id: Int): TodoItem{
        return _todoItems.value!!.find { it.id == id }!!
    }

    fun updateRecycleItem(item: RecycleItem, flg: Boolean){
        val list = _recycleItems.value!!.toMutableList()
        list[list.lastIndexOf(item)].isFinish = flg
        _recycleItems.value = list.toList()
    }
}