package com.nisilab.simpletodo.recycle

import android.view.View
import com.airbnb.epoxy.Typed2EpoxyController
import com.nisilab.simpletodo.di.database.TodoItem
import com.nisilab.simpletodo.todoItem

class ItemDataBindingViewController(
    private val selectListener: SelectListener
): Typed2EpoxyController<List<RecycleItem>,Boolean>() {
    override fun buildModels(items: List<RecycleItem>, loadingMore: Boolean) {
        items.forEach { item ->
            todoItem {
                title(item.title)
                deadLine(item.deadLine.toString())
                tag(item.tag)
                text(item.text)
                onClickOpenButton( View.OnClickListener { selectListener.onClickOpenButton() } )
                onClickCloseButton( View.OnClickListener { selectListener.onClickCloseButton() } )
            }
        }
    }
    interface SelectListener {
        fun onClickOpenButton()
        fun onClickCloseButton()
    }
}