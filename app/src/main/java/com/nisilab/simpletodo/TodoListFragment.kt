package com.nisilab.simpletodo

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nisilab.simpletodo.databinding.FragmentTodoListBinding
import com.nisilab.simpletodo.di.viewmodel.ListViewModel
import com.nisilab.simpletodo.dialog.ConfirmationDialogFragment
import com.nisilab.simpletodo.recycle.ItemDataBindingViewController
import com.nisilab.simpletodo.recycle.RecycleItem
import dagger.hilt.android.AndroidEntryPoint
import java.sql.Date
import java.time.format.DateTimeFormatter

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
class TodoListFragment : Fragment(),ConfirmationDialogFragment.DialogSelectedListener {
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
        val binding = DataBindingUtil.inflate<FragmentTodoListBinding>(inflater,
            R.layout.fragment_todo_list,container,false)

        viewModel.setAllItems()

        // backButtonのクリックリスナーの設定
        binding.toEditButton.setOnClickListener {
            findNavController().navigate(R.id.action_todoListFragment_to_todoEditFragment)
        }

        // recycleItemに使われるクリックリスナーの設定
        val itemsViewController = ItemDataBindingViewController(object :
            ItemDataBindingViewController.SelectListener {
            override fun onClickOpenButton(item: RecycleItem) {
                viewModel.setOpenFlag(item)
            }

            override fun onClickCloseButton(item: RecycleItem) {
                viewModel.setCloseFlag(item)
            }

            override fun onClickFinishButton(item: RecycleItem) {
                viewModel.updateItem(item,false)
            }

            override fun onClickNoFinishButton(item: RecycleItem) {
                viewModel.updateItem(item,true)
            }

            override fun onClickDeleteButton(item: RecycleItem) {
                ConfirmationDialogFragment.Builder(parentFragment!!)
                    .setId(item.id)
                    .setTitle(item.title)
                    .build()
                    .show(childFragmentManager,"Confirmation")

            }

            override fun onClickEditButton(item: RecycleItem) {
                val df = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val tf = DateTimeFormatter.ofPattern("HH:mm")
                val action = TodoListFragmentDirections.actionTodoListFragmentToTodoEditFragment(
                    itemId = item.id,
                    itemTitle = item.title,
                    itemTag = item.tag,
                    itemText = item.text,
                    itemDate = item.deadLine.format(df).toString(),
                    itemTime = item.deadLine.format(tf).toString()
                )
                findNavController().navigate(action)
            }
        })

        // todoListのアダプターを適応
        binding.todoList.apply {
            this.adapter = itemsViewController.adapter
            this.layoutManager = LinearLayoutManager(context).apply {
                orientation = LinearLayoutManager.VERTICAL
            }
        }

        viewModel.todoItems.observe(viewLifecycleOwner){
            viewModel.setRecycleItems()
        }

        viewModel.recycleItems.observe(viewLifecycleOwner){
            viewModel.setOutItems()
        }

        viewModel.outRecycleItems.observe(viewLifecycleOwner){
            itemsViewController.setData(it,false)
        }

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