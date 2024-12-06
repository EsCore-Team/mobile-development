package com.dicoding.escore.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.escore.data.remote.UserRepository
import com.dicoding.escore.data.remote.response.LoginResponse
import com.dicoding.escore.data.remote.Result
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

//class LoginViewModel(private val repository: UserRepository) : ViewModel() {
//    private val _isLoading = MutableLiveData<Boolean>()
//    val isLoading: LiveData<Boolean> = _isLoading
//
//    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
//    val loginResult: LiveData<Result<LoginResponse>> = _loginResult
//
//    fun login(email: String, password: String) {
//        _isLoading.value = true
//        viewModelScope.launch {
//            try {
//                val result = repository.login(email, password)
//                _loginResult.value = result
//            } catch (e: IOException) {
//                // Pastikan pengecualian ini tertangkap dengan baik
//                _loginResult.value = Result.Error("Connection error. Please check your internet connection.")
//            } catch (e: Exception) {
//                // Tangani pengecualian lainnya
//                _loginResult.value = Result.Error(e.message ?: "An unexpected error occurred.")
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }
//}

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> = _loginResult

    fun login(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = repository.login(email, password)
                _loginResult.value = result
            } catch (e: IOException) {
                // Tangani masalah koneksi
                _loginResult.value = Result.Error("Connection error. Please check your internet connection.")
            } catch (e: HttpException) {
                // Tangani error HTTP (termasuk 401 Unauthorized)
                if (e.code() == 401) {
                    _loginResult.value = Result.Error("Email and password not found")
                } else {
                    // Tangani error HTTP lainnya
                    _loginResult.value = Result.Error("An error occurred: ${e.message()}")
                }
            } catch (e: Exception) {
                // Tangani pengecualian lainnya
                _loginResult.value = Result.Error(e.message ?: "An unexpected error occurred.")
            } finally {
                _isLoading.value = false
            }
        }
    }
}
