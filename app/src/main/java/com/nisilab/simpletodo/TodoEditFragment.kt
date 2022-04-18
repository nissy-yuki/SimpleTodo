package com.nisilab.simpletodo

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.nisilab.simpletodo.databinding.FragmentTodoEditBinding
import com.nisilab.simpletodo.di.viewmodel.EditViewModel
import com.nisilab.simpletodo.dialog.DatePick
import com.nisilab.simpletodo.dialog.TimePick
import dagger.hilt.android.AndroidEntryPoint
import java.lang.NullPointerException

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
class TodoEditFragment : Fragment(), DatePick.OnSelectedDateListener, TimePick.OnSelectedTimeListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private val viewModel: EditViewModel by viewModels()
    private lateinit var binding: FragmentTodoEditBinding

    private val args: TodoEditFragmentArgs by navArgs()

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
        args.itemData?.let { item ->
            viewModel.setInitialItem(item)
        }

        binding = DataBindingUtil.inflate<FragmentTodoEditBinding>(inflater,
            R.layout.fragment_todo_edit,container,false)

        // dateEditorをタップすると日付選択ダイアログの表示
        binding.dateEditor.setOnClickListener {
            val newFragment = DatePick(viewModel.editDate.value)
            newFragment.show(childFragmentManager, "datePicker")
        }

        // timeEditorをタップすると時刻選択ダイアログの表示
        binding.timeEditor.setOnClickListener {
            val newFragment = TimePick(viewModel.editTime.value)
            newFragment.show(childFragmentManager, "timePicker")
        }


        // 以下三つは画面回転対応用の値保存
//        binding.titleEditor.setText(viewModel.editTitle.value)
//        binding.titleEditor.addTextChangedListener {
//            viewModel.setEditTitle(it.toString())
//        }

        binding.tagEditor.setText(viewModel.editTag.value)
        binding.tagEditor.addTextChangedListener {
            viewModel.setEditTag(it.toString())
        }

        binding.textEditor.setText(viewModel.editText.value)
        binding.textEditor.addTextChangedListener {
            viewModel.setEditText(it.toString())
        }

        // dateEditorへの書き込み
        viewModel.editDate.observe(viewLifecycleOwner){
            viewModel.setDeadLine()
            binding.dateText = it.toString()
        }

        // timeEditorへの書き込み
        viewModel.editTime.observe(viewLifecycleOwner){
            viewModel.setDeadLine()
            binding.timeText = it.toString()
        }

        binding.titleText.setContent { textLabel(value = "title") }
        binding.deadLineText.setContent { textLabel(value = "deadLine") }
        binding.tagText.setContent { textLabel(value = "tag") }
        binding.textText.setContent{ textLabel(value = "text") }
        
        binding.toListButton.setContent { 
            Button(onClick = { findNavController().popBackStack() }) {
                Text(text = "back")
        } }
        
        binding.saveButton.setContent { Button(onClick = {
            if (!viewModel.editTitle.value.isNullOrBlank() && !viewModel.editDeadLine.value.toString().isNullOrBlank()) {
                viewModel.addItem()
                findNavController().navigate(R.id.action_todoEditFragment_to_todoListFragment)
            } else {
                val message = "タイトル、または日時を入力してください"
                Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
            }
        }) {
            Text(text = "save")
        } }
        binding.titleEditor.setContent {
            elmTextField(value = viewModel.editTitle.value ?: "", changeValue = { viewModel.setEditTitle(it) }, hint = "title")
        }
        viewModel.editTitle.observe(viewLifecycleOwner){
            binding.titleEditor.setContent {
                elmTextField(value = viewModel.editTitle.value ?: "", changeValue = { viewModel.setEditTitle(it) }, hint = "title")
            }
        }




        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val editors: List<EditText> = listOf(binding.tagEditor,binding.textEditor)

        editors.forEach { item ->
            item.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    //キーボード非表示
                    val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                }
            }
        }

        view.setOnTouchListener { v, event ->
            view.requestFocus()
            v?.onTouchEvent(event) ?: true
        }
    }

    // 日付選択のOKボタンのクリックリスナー
    override fun selectedDate(year: Int, month: Int, day: Int) {
        viewModel.setEditDate("%4d-%02d-%02d".format(year, month, day))
    }

    // 時刻選択のOKボタンのクリックリスナー
    override fun selectedTime(hour: Int, minute: Int) {
        viewModel.setEditTime("%02d:%02d".format(hour, minute))
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
}

@Composable
fun textLabel(value: String) = Text(value, fontSize = 20.sp)

@Composable
fun backButton(toBack: Boolean) = Button(onClick = {toBack} ){ Text(text = "back") }

@Composable
fun elmTextField(value: String,changeValue: (String) -> Unit,hint: String){
    TextField(value = value, onValueChange = changeValue, placeholder = { Text(text = hint) })
}