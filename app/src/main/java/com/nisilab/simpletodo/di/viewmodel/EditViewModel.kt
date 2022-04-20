package com.nisilab.simpletodo.di.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nisilab.simpletodo.di.database.TodoItem
import com.nisilab.simpletodo.di.repository.DataRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject

class EditViewModel@ViewModelInject constructor(
    private val repository: DataRepository
): ViewModel() {

    private val _editTitle: MutableLiveData<String> = MutableLiveData()
    private val _editDeadLine: MutableLiveData<LocalDateTime> = MutableLiveData()
    private val _editTag: MutableLiveData<String> = MutableLiveData()
    private val _editText: MutableLiveData<String> = MutableLiveData()

    private val _editDate: MutableLiveData<LocalDate> = MutableLiveData(LocalDate.now())
    private val _editTime: MutableLiveData<LocalTime> = MutableLiveData(LocalTime.now().truncatedTo(ChronoUnit.MINUTES))

    private val _editId: MutableLiveData<Int> = MutableLiveData(0)

    val editTitle: LiveData<String> = _editTitle
    val editTag: LiveData<String> = _editTag
    val editText: LiveData<String> = _editText

    val editDate: LiveData<LocalDate> = _editDate
    val editTime: LiveData<LocalTime> = _editTime
    val editDeadLine: LiveData<LocalDateTime> = _editDeadLine

    fun setInitialItem(item: TodoItem){
        _editId.value = item.id
        setEditTitle(item.title)
        item.tag?.let { setEditTag(it) }
        item.text?.let { setEditText(it) }
        setDeadLine(item.deadLine)
    }


    fun addItem(){
        val item = TodoItem(
            id = _editId.value!!,
            title = _editTitle.value!!,
            deadLine = _editDeadLine.value!!,
            tag = _editTag.value,
            text = _editText.value
        )
        viewModelScope.launch {

            if (_editId.value == 0){
                repository.addItem(item)
            } else {
                repository.updateItem(item)
            }

        }
    }

    // 各
    fun setEditTitle(value: String) {
        _editTitle.value = value
    }

    fun setEditTag(value: String){
        _editTag.value = value
    }

    fun setEditText(value: String){
        _editText.value = value
    }

    fun setEditDate(value: String){
        _editDate.value = LocalDate.parse(value)
    }

    fun setEditTime(value: String){
        _editTime.value = LocalTime.parse(value)
    }

    fun setDeadLine(){
        if(_editDate.value != null && _editTime.value != null) _editDeadLine.value = LocalDateTime.of(_editDate.value,_editTime.value!!.truncatedTo(ChronoUnit.MINUTES))
    }

    fun setDeadLine(dl: LocalDateTime){
        _editDeadLine.value = dl
        _editDate.value = dl.toLocalDate()
        _editTime.value = dl.toLocalTime()
    }


}