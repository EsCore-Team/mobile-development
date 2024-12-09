package com.dicoding.escore.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.escore.data.remote.UserRepository
import com.dicoding.escore.data.remote.response.SignUpResponse
import com.dicoding.escore.data.remote.Result
import kotlinx.coroutines.launch
import retrofit2.HttpException

class SignUpViewModel(private val repository: UserRepository): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _registerResult = MutableLiveData<Result<SignUpResponse>>()
    val registerResult: LiveData<Result<SignUpResponse>> = _registerResult

    fun register(fullName: String, email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = repository.register(fullName, email, password)
                _registerResult.value = result
            } catch (e: HttpException) {
                // Tangani error HTTP
                when (e.code()) {
                    400 -> {
                        _registerResult.value = Result.Error("Email already exists!")
                    }
                    else -> {
                        // Tangani error HTTP lainnya
                        _registerResult.value = Result.Error("An error occurred: ${e.message()}")
                    }
                }
            } catch (e: Exception) {
                _registerResult.value = Result.Error(e.message ?: "An error occurred.")
            } finally {
                _isLoading.value = false
            }
        }
    }
}
