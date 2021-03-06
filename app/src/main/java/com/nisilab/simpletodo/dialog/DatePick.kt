package com.nisilab.simpletodo.dialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.time.LocalDate
import java.util.*

class DatePick(private val iDate: LocalDate?): DialogFragment(), DatePickerDialog.OnDateSetListener {

    interface OnSelectedDateListener {
        fun selectedDate(year: Int, month: Int, day: Int)
    }

    private lateinit var listener: OnSelectedDateListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = parentFragment as OnSelectedDateListener
        } catch (e: ClassCastException) {
            throw ClassCastException(
                (context.toString() +
                        " must implement NoticeDialogListener")
            )
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = iDate?.year ?: c.get(Calendar.YEAR)
        val month = iDate?.monthValue ?: c.get(Calendar.MONTH)
        val day = iDate?.dayOfMonth ?: c.get(Calendar.DAY_OF_MONTH)

        return DatePickerDialog(requireContext(), this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        this.listener.selectedDate(year,month,day)
    }
}