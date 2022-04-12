package com.nisilab.simpletodo.recycle

import java.time.LocalDateTime

data class RecycleItem(
    val id: Int = 0,
    val title: String,
    val deadLine: LocalDateTime,
    val tag: String?,
    val text: String?,
    val isFinish: Boolean,
    var isOpen: Boolean
)
