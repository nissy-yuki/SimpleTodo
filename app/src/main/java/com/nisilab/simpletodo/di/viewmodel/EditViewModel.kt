package com.nisilab.simpletodo.di.viewmodel

import android.util.Log
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
import javax.inject.Inject

class EditViewModel@ViewModelInject constructor(
    private val repository: DataRepository
): ViewModel() {

    private val _editTitle: MutableLiveData<String> = MutableLiveData()
    private val _editDeadLine: MutableLiveData<LocalDateTime> = MutableLiveData()
    private val _editTag: MutableLiveData<String> = MutableLiveData()
    private val _editText: MutableLiveData<String> = MutableLiveData()

    private val _editDate: MutableLiveData<LocalDate> = MutableLiveData()
    private val _editTime: MutableLiveData<LocalTime> = MutableLiveData()

    val editTitle: LiveData<String> = _editTitle
    val editDeadLine: LiveData<LocalDateTime> = _editDeadLine
    val editTag: LiveData<String> = _editTag
    val editText: LiveData<String> = _editText

    val editDate: LiveData<LocalDate> = _editDate
    val editTime: LiveData<LocalTime> = _editTime

    fun addItem(){
        viewModelScope.launch {
            repository.addItem(
                TodoItem(
                    id = 0,
                    title = editTitle.value!!,
                    deadLine = editDeadLine.value!!,
                    tag = editTag.value,
                    text = editText.value
                )
            )
        }
    }

    fun setEditTitle(value: String){
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
        Log.d("check","${_editDate.value} ${_editTime.value}")
//        if(_editDate.value ) _editDeadLine.value = LocalDateTime.of(_editDate.value,_editTime.value)
    }

}