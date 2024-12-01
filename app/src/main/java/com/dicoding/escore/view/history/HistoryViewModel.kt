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

class HistoryViewModel(
    private val repository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _historyLiveData = MutableLiveData<HistoryResponse>()
    val historyLiveData: LiveData<HistoryResponse> get() = _historyLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorLiveData

    fun fetchHistory(createdAt: String, title: String, score: String) {
        viewModelScope.launch {
            try {
                val email = sessionManager.getUserEmail()
                if (email.isNullOrEmpty()) {
                    _errorLiveData.postValue("Email not found in session.")
                    return@launch
                }

                val response = repository.getHistory(email, createdAt, title, score)
                _historyLiveData.postValue(response)
            } catch (e: Exception) {
                _errorLiveData.postValue(e.message ?: "An error occurred.")
            }
        }
    }
}
