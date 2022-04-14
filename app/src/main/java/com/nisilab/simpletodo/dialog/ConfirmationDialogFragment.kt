package com.nisilab.simpletodo.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class ConfirmationDialogFragment: DialogFragment() {

    interface DialogSelectedListener{
        fun onPositiveButton()
    }

    private lateinit var listener: DialogSelectedListener

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
        builder.setTitle("Here Title")
            .setMessage("Here Message")
            .setPositiveButton("done") { dialog, id ->
                dismiss()
                listener.onPositiveButton()
            }
            .setNegativeButton("cancel") { dialog, id ->
                dismiss()
            }

        return builder.create()
    }
}