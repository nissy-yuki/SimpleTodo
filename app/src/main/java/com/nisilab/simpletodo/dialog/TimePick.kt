package com.nisilab.simpletodo.dialog

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class TimePick: DialogFragment(),TimePickerDialog.OnTimeSetListener {

    interface OnSelectedTimeListener {
        fun selectedTime(hour: Int, minute: Int)
    }

    private lateinit var listener: OnSelectedTimeListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = parentFragment as OnSelectedTimeListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException(
                (context.toString() +
                        " must implement NoticeDialogListener")
            )
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        return TimePickerDialog(requireContext(),this,hour,minute,true)
    }

    override fun onTimeSet(view: TimePicker, hour: Int, minute: Int) {
        this.listener.selectedTime(hour, minute)
    }
}