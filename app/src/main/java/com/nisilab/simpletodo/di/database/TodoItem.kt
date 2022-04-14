package com.nisilab.simpletodo.di.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nisilab.simpletodo.recycle.RecycleItem
import kotlinx.android.parcel.Parcelize
import java.time.LocalDateTime

@Parcelize
@Entity(tableName = "todo_table")
data class TodoItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var title: String,
    var deadLine: LocalDateTime,
    var tag: String?,
    var text: String?,
    var isFinish: Boolean = false
) : Parcelable {
    fun toRecycleItem(): RecycleItem{
        return RecycleItem(
            id = this.id,
            title = this.title,
            deadLine = this.deadLine,
            tag = this.tag,
            text = this.text,
            isFinish = this.isFinish,
            isOpen = false
        )
    }
}
