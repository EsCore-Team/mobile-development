package com.dicoding.escore.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.escore.data.remote.UserRepository
import com.dicoding.escore.data.remote.response.SignUpResponse
import com.dicoding.escore.data.remote.Result
import kotlinx.coroutines.launch

class SignUpViewModel(private val repository: UserRepository): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _registerResult = MutableLiveData<Result<SignUpResponse>>()
    val registerResult: LiveData<Result<SignUpResponse>> = _registerResult

    fun register(fullName: String, email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            val result = repository.register(fullName, email, password)
            _registerResult.value = result
            _isLoading.value = false
        }
    }
}