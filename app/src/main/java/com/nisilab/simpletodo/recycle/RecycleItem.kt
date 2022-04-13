package com.nisilab.simpletodo.recycle

import com.nisilab.simpletodo.di.database.TodoItem
import java.time.LocalDateTime

data class RecycleItem(
    val id: Int = 0,
    val title: String,
    val deadLine: LocalDateTime,
    val tag: String?,
    val text: String?,
    val isFinish: Boolean,
    var isOpen: Boolean
){
    fun toTodoItem(): TodoItem{
        return TodoItem(
            id = this.id,
            title = this.title,
            deadLine = this.deadLine,
            tag = this.tag,
            text = this.text,
            isFinish = this.isFinish
        )
    }
}
