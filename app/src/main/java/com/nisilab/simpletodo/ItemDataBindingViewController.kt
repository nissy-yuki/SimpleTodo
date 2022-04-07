package com.nisilab.simpletodo

import android.view.View
import com.airbnb.epoxy.Typed2EpoxyController
import com.nisilab.simpletodo.di.database.TodoItem

class ItemDataBindingViewController(
    private val selectListener: SelectListener
): Typed2EpoxyController<List<TodoItem>,Boolean>() {
    override fun buildModels(items: List<TodoItem>, loadingMore: Boolean) {
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