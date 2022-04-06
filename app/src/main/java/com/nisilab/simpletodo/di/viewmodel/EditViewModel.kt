package com.nisilab.simpletodo.di.viewmodel

import androidx.lifecycle.ViewModel
import com.nisilab.simpletodo.di.repository.DataRepository
import javax.inject.Inject

class EditViewModel@Inject constructor(
    repository: DataRepository
): ViewModel() {
}