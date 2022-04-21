package com.nisilab.simpletodo


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.fragment.findNavController
import com.nisilab.simpletodo.databinding.FragmentTodoListBinding
import com.nisilab.simpletodo.di.viewmodel.ListViewModel
import com.nisilab.simpletodo.dialog.ConfirmationDialogFragment
import com.nisilab.simpletodo.recycle.RecycleItem
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TodoListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

@AndroidEntryPoint
class TodoListFragment : Fragment(), ConfirmationDialogFragment.DialogSelectedListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val viewModel: ListViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentTodoListBinding>(
            inflater,
            R.layout.fragment_todo_list, container, false
        )
        binding.listScreen.setContent {

            viewModel.setAllItems()

            listScreen(viewModel = viewModel) {
                val action = TodoListFragmentDirections.actionTodoListFragmentToTodoEditFragment(
                    itemData = null
                )
                findNavController().navigate(action)
            }

        }

//        // recycleItemに使われるクリックリスナーの設定
//        val itemsViewController = ItemDataBindingViewController(object :
//            ItemDataBindingViewController.SelectListener {
//            override fun onClickOpenButton(item: RecycleItem) {
//                viewModel.setOpenFlag(item)
//            }
//
//            override fun onClickCloseButton(item: RecycleItem) {
//                viewModel.setCloseFlag(item)
//            }
//
//            override fun onClickFinishButton(item: RecycleItem) {
//                viewModel.updateItem(item,false)
//            }
//
//            override fun onClickNoFinishButton(item: RecycleItem) {
//                viewModel.updateItem(item,true)
//            }
//
//            override fun onClickDeleteButton(item: RecycleItem) {
//                ConfirmationDialogFragment.Builder(parentFragment!!)
//                    .setId(item.id)
//                    .setTitle(item.title)
//                    .build()
//                    .show(childFragmentManager,"Confirmation")
//
//            }
//
//            override fun onClickEditButton(item: RecycleItem) {
//                val action = TodoListFragmentDirections.actionTodoListFragmentToTodoEditFragment(
//                    itemData = item.toTodoItem()
//                )
//                findNavController().navigate(action)
//            }
//        })
//
//        // todoListのアダプターを適応
//        binding.todoList.apply {
//            this.adapter = itemsViewController.adapter
//            this.layoutManager = LinearLayoutManager(context).apply {
//                orientation = LinearLayoutManager.VERTICAL
//            }
//        }
//

//        viewModel.outRecycleItems.observe(viewLifecycleOwner){
//            itemsViewController.setData(it,false)
//        }

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TodoListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TodoListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onPositiveButton(id: Int) {
        viewModel.deleteItem(id)
    }
}

@Composable
fun listScreen(viewModel: ListViewModel = viewModel(), toEdit: () -> Unit) {

    val listItems by viewModel.recycleItems.observeAsState()

    Log.d("checklist", "$listItems")

    SideEffect {
        Log.d("MainScreen", "composition!")
    }



    todoList(
        listItems = listItems,
        changeFinishFlgAction = viewModel::updateItem,
        changeOpenFlgAction = viewModel::changeOpenFlag
    )
    toEditFab {
        toEdit()
    }

}

@Composable
fun todoList(
    listItems: List<RecycleItem>?,
    changeFinishFlgAction: (RecycleItem) -> Unit,
    changeOpenFlgAction: (RecycleItem) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        listItems?.forEach { todo ->
            item {
                todoListItem(
                    item = todo,
                    checkClickAction = { changeFinishFlgAction(todo) },
                    changeOpenFlgAction = { changeOpenFlgAction(todo ) }
                )
            }
        }
    }
}

@Composable
fun todoListItem(
    item: RecycleItem,
    checkClickAction: () -> Unit,
    changeOpenFlgAction: () -> Unit
) {
    SideEffect {
        Log.d("listItem", "composition! ${item.id}")
    }
    Card(shape = RoundedCornerShape(20.dp)) {
        Column(modifier = Modifier.clickable(enabled = true,
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple(bounded = false)) {
            changeOpenFlgAction
        }) {
            closeItemSet(item = item, checkClickAction = checkClickAction, arrowClickAction = changeOpenFlgAction)
        }
    }
}

@Composable
fun closeItemSet(item: RecycleItem, checkClickAction: () -> Unit, arrowClickAction: () -> Unit){
    Row(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        checkButton(item = item, action = checkClickAction)
        listItemTitleSet(title = item.title, deadLine = item.deadLine.toString())
        arrowButton(item = item, action = arrowClickAction)
    }
}

@Composable
fun arrowButton(item: RecycleItem, action: () -> Unit){
    Box(modifier = Modifier
        .fillMaxHeight()
        .fillMaxWidth(), contentAlignment = Alignment.BottomEnd) {
        val iconImg = if (!item.isOpen) Icons.Filled.ArrowDropDown else Icons.Filled.ArrowDropUp
        Icon(iconImg, contentDescription = "Open", modifier = Modifier.clickable(
            enabled = true,
            interactionSource = remember { MutableInteractionSource() },
            indication = rememberRipple(bounded = false)
        ) {
            action()
        })
    }
}

@Composable
fun checkButton(item: RecycleItem,action: () -> Unit){
    Icon(
        Icons.Filled.Check,
        contentDescription = "check",
        modifier = Modifier
            .size(50.dp)
            .padding(end = 16.dp)
            .clickable(
                enabled = true,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = false)
            ) {
                action()
            },
        if (!item.isFinish) Color.Gray else Color.Green
    )
}

@Composable
fun listItemTitleSet(title: String, deadLine: String){
    Column(modifier = Modifier.padding(top = 5.dp)) {
        Text(text = deadLine, fontSize = 10.sp)
        Text(text = title, fontSize = 15.sp)
    }
}


@Composable
fun toEditFab(action: () -> Unit) {
    Box(contentAlignment = Alignment.BottomEnd) {
        FloatingActionButton(modifier = Modifier.padding(32.dp), onClick = { action() }) {
            Icon(Icons.Filled.Add, contentDescription = "add")
        }
    }
}