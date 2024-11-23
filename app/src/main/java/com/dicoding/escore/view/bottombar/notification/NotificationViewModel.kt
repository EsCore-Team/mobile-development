package com.dicoding.escore.view.bottombar.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NotificationViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is notification Fragment"
    }
    val text: LiveData<String> = _text
}