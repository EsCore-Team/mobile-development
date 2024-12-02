package com.dicoding.escore.view.detailHistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.escore.data.remote.UserRepository
import com.dicoding.escore.data.remote.response.PredictionsItem
import com.dicoding.escore.pref.SessionManager
import kotlinx.coroutines.launch

class DetailHistoryViewModel(
    private val repository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _detailLiveData = MutableLiveData<PredictionsItem?>()
    val detailLiveData: MutableLiveData<PredictionsItem?> get() = _detailLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorLiveData

    fun getUserEmail(): String? {
        return sessionManager.getUserEmail()
    }

    fun fetchDetailHistory(email: String, id: String) {
        viewModelScope.launch {
            try {
                val response = repository.getDetailHistory(email, id)
                val prediction = response.predictions?.find { it?.id == id }
                if (prediction != null) {
                    _detailLiveData.postValue(prediction)
                } else {
                    _errorLiveData.postValue("Detail not found.")
                }
            } catch (e: Exception) {
                _errorLiveData.postValue(e.message ?: "An error occurred.")
            }
        }
    }
}
