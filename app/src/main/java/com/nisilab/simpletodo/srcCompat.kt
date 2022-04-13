package com.nisilab.simpletodo

import android.widget.ImageView
import androidx.databinding.BindingAdapter

@BindingAdapter("srcCompat")
fun srcCompat (view: ImageView, resourceId: Int){
    view.setImageResource(resourceId)
}