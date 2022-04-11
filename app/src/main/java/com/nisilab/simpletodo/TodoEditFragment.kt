package com.nisilab.simpletodo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.nisilab.simpletodo.databinding.FragmentTodoEditBinding
import com.nisilab.simpletodo.databinding.FragmentTodoListBinding
import com.nisilab.simpletodo.di.viewmodel.EditViewModel
import dagger.hilt.android.AndroidEntryPoint

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TodoEditFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

@AndroidEntryPoint
class TodoEditFragment : Fragment(), DatePick.OnSelectedTimeListener, TimePick.OnSelectedTimeListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val viewModel: EditViewModel by viewModels()

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
        val binding = DataBindingUtil.inflate<FragmentTodoEditBinding>(inflater,
            R.layout.fragment_todo_edit,container,false)

        // 保存ボタンのクリックリスナーの設定
        binding.saveButton.setOnClickListener {
            if(!binding.titleEditor.text.isNullOrBlank() && !binding.dateEditor.text.isNullOrBlank() && !binding.timeEditor.text.isNullOrBlank()){
                findNavController().navigate(R.id.action_todoEditFragment_to_todoListFragment)
            } else {
                val message: String = "タイトル、または日時を入力してください"
                Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
            }
        }

        binding.toListButton.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.dateEditor.setOnClickListener {
            val newFragment = DatePick()
            newFragment.show(childFragmentManager, "datePicker")
        }

        binding.timeEditor.setOnClickListener {
            val newFragment = DatePick()
            newFragment.show(childFragmentManager, "datePicker")
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
         * @return A new instance of fragment TodoEditFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TodoEditFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun selectedDate(year: Int, month: Int, day: Int) {

    }

    override fun selectedTime(hour: Int, minute: Int) {
        TODO("Not yet implemented")
    }
}