package com.dicoding.escore.view.bottombar.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.escore.pref.SessionManager

class ProfileViewModel(private val sessionManager: SessionManager) : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        val email = sessionManager.getUserEmail() ?: "Email tidak tersedia"
        value = email
    }
    val text: LiveData<String> = _text
}
