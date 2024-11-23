package com.dicoding.escore.view.bottombar.guide

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GuideViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is guide Fragment"
    }
    val text: LiveData<String> = _text
}