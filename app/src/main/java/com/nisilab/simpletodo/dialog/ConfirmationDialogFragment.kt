package com.nisilab.simpletodo.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.nisilab.simpletodo.di.database.TodoItem

class ConfirmationDialogFragment: DialogFragment() {

    interface DialogSelectedListener{
        fun onPositiveButton(id: Int)
    }

    private lateinit var listener: DialogSelectedListener

    private var title = ""
    private var iId = 0

    companion object {
        private const val TITLE_KEY = "TitleKey"
        private const val ID_KEY = "idKey"
    }


    class Builder(private val fragment: Fragment) {
        private val bundle = Bundle()

        fun setTitle(title: String): Builder {
            return this.apply {
                bundle.putString(TITLE_KEY,title)
            }
        }

        fun setId(id: Int): Builder{
            return this.apply {
                bundle.putInt(ID_KEY,id)
            }
        }

        fun build(): ConfirmationDialogFragment {
            return ConfirmationDialogFragment().apply {
                arguments = bundle
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = parentFragment as DialogSelectedListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                (context.toString() +
                        " must implement NoticeDialogListener")
            )
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())

        arguments?.let {
            title = it.getString(TITLE_KEY, "")
            iId = it.getInt(ID_KEY, 0)
        }

        builder.setTitle(title)
            .setMessage("このTodoを消していいですか？")
            .setPositiveButton("done") { dialog, id ->
                dismiss()
                listener.onPositiveButton(iId)
            }
            .setNegativeButton("cancel") { dialog, id ->
                dismiss()
            }

        return builder.create()
    }
}