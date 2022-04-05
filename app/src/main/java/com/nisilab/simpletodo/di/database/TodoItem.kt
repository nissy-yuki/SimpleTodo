package com.nisilab.simpletodo.di.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "todo_table")
data class TodoItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var title: String,
    var deadLine: LocalDateTime,
    var tag: String?,
    var text: String?,
    var isFinish: Boolean = false
)
