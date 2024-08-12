package com.cashflowtracker.miranda.ui.recurrents

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecurrentsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is recurrents Fragment"
    }
    val text: LiveData<String> = _text
}