package com.nisilab.simpletodo.recycle

import android.util.Log
import android.view.View
import com.airbnb.epoxy.Typed2EpoxyController
import com.nisilab.simpletodo.noDataItem
import com.nisilab.simpletodo.todoItem

class ItemDataBindingViewController(
    private val selectListener: SelectListener
): Typed2EpoxyController<List<RecycleItem>,Boolean>() {
    override fun buildModels(items: List<RecycleItem>, loadingMore: Boolean) {
        if(!items.isNullOrEmpty()){
            items.forEach { item ->
                todoItem {
                    id("todo_item"+item.id)
                    title(item.title)
                    deadLine(item.deadLine.toString())
                    tag(if(!item.tag.isNullOrBlank()) item.tag else "non")
                    text(if(!item.text.isNullOrBlank()) item.text else "non")
                    isOpen(item.isOpen)
                    isFinish(item.isFinish)
                    Log.d("checkValue","${item.isFinish}")
                    onClickFinishButton( View.OnClickListener { selectListener.onClickFinishButton(item) })
                    onClickNoFinishButton( View.OnClickListener { selectListener.onClickNoFinishButton(item)  })
                    onClickOpenButton( View.OnClickListener { selectListener.onClickOpenButton(item) } )
                    onClickCloseButton( View.OnClickListener { selectListener.onClickCloseButton(item) } )
                }
            }
        }else{
            noDataItem {
                id("no_data_item")
                text("no data")
            }
        }

    }

    interface SelectListener {
        fun onClickFinishButton(item: RecycleItem)
        fun onClickNoFinishButton(item: RecycleItem)
        fun onClickOpenButton(item: RecycleItem)
        fun onClickCloseButton(item: RecycleItem)
    }
}