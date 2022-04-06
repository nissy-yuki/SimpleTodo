package com.nisilab.simpletodo.di.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nisilab.simpletodo.di.database.TodoItem
import com.nisilab.simpletodo.di.repository.DataRepository
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

class EditViewModel@Inject constructor(
    private val repository: DataRepository
): ViewModel() {

    private val _editTitle: MutableLiveData<String> = MutableLiveData()
    private val _editDeadLine: MutableLiveData<LocalDateTime> = MutableLiveData()
    private val _editTag: MutableLiveData<String> = MutableLiveData()
    private val _editText: MutableLiveData<String> = MutableLiveData()

    val editTitle: LiveData<String> = _editTitle
    val editDeadLine: LiveData<LocalDateTime> = _editDeadLine
    val editTag: LiveData<String> = _editTag
    val editText: LiveData<String> = _editText

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

}