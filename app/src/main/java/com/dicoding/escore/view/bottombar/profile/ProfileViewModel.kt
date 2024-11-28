package com.dicoding.escore.view.bottombar.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.escore.pref.SessionManager

class ProfileViewModel(private val sessionManager: SessionManager) : ViewModel() {
    private val _email = MutableLiveData<String>().apply {
        value = sessionManager.getUserEmail() ?: "Email tidak tersedia"
    }
    val email: LiveData<String> = _email

    private val _fullName = MutableLiveData<String>().apply {
        value = sessionManager.getUserFullName() ?: "Nama lengkap tidak tersedia"
    }
    val fullName: LiveData<String> = _fullName
}
