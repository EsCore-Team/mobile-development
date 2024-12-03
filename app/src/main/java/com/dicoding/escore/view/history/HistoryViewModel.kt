package com.dicoding.escore.view.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.escore.data.remote.Result
import com.dicoding.escore.data.remote.UserRepository
import com.dicoding.escore.data.remote.response.HistoryResponse
import com.dicoding.escore.data.remote.response.LoginResponse
import com.dicoding.escore.pref.SessionManager
import kotlinx.coroutines.launch

//class HistoryViewModel(
//    private val repository: UserRepository,
//    private val sessionManager: SessionManager
//) : ViewModel() {
//
//    private val _historyLiveData = MutableLiveData<HistoryResponse>()
//    val historyLiveData: LiveData<HistoryResponse> get() = _historyLiveData
//
//    private val _errorLiveData = MutableLiveData<String>()
//    val errorLiveData: LiveData<String> get() = _errorLiveData
//
//    private val _isLoading = MutableLiveData<Boolean>()
//    val isLoading: LiveData<Boolean> = _isLoading
//
//    fun fetchHistory(createdAt: String, title: String, score: String) {
//        viewModelScope.launch {
//            _isLoading.postValue(true) // Mulai loading
//            try {
//                val email = sessionManager.getUserEmail()
//                if (email.isNullOrEmpty()) {
//                    _errorLiveData.postValue("Email not found in session.")
//                    return@launch
//                }
//                val response = repository.getHistory(email, createdAt, title, score)
//                _historyLiveData.postValue(response)
//            } catch (e: Exception) {
//                _errorLiveData.postValue(e.message ?: "An error occurred.")
//            } finally {
//                _isLoading.postValue(false) // Selesai loading
//            }
//        }
//    }
//
//}

class HistoryViewModel(
    private val repository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _historyLiveData = MutableLiveData<Result<HistoryResponse>>()
    val historyLiveData: LiveData<Result<HistoryResponse>> get() = _historyLiveData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun fetchHistory(createdAt: String, title: String, score: String) {
        viewModelScope.launch {
            _historyLiveData.postValue(Result.Loading) // Indikasikan loading
            try {
                val email = sessionManager.getUserEmail()
                if (email.isNullOrEmpty()) {
                    _historyLiveData.postValue(Result.Error("Email not found in session."))
                    return@launch
                }
                val response = repository.getHistory(email, createdAt, title, score)
                _historyLiveData.postValue(Result.Success(response))
            } catch (e: Exception) {
                _historyLiveData.postValue(Result.Error(e.message ?: "An error occurred."))
            }
        }
    }
}

