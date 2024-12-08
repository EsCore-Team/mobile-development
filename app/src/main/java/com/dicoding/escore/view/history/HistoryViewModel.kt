package com.dicoding.escore.view.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.escore.data.remote.Result
import com.dicoding.escore.data.remote.UserRepository
import com.dicoding.escore.data.remote.response.HistoryResponse
import com.dicoding.escore.pref.SessionManager
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException


class HistoryViewModel(
    private val repository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _historyLiveData = MutableLiveData<Result<HistoryResponse>>()
    val historyLiveData: LiveData<Result<HistoryResponse>> get() = _historyLiveData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // MutableLiveData untuk mengontrol visibilitas "No Data History"
    private val _noDataVisible = MutableLiveData<Boolean>()
    val noDataVisible: LiveData<Boolean> = _noDataVisible

    fun fetchHistory(createdAt: String, title: String, score: String) {
        viewModelScope.launch {
            _historyLiveData.postValue(Result.Loading)
            _isLoading.postValue(true)

            try {
                val email = sessionManager.getUserEmail()
                if (email.isNullOrEmpty()) {
                    _historyLiveData.postValue(Result.Error("Email not found in session."))
                    _isLoading.postValue(false)
                    _noDataVisible.postValue(true)
                    return@launch
                }

                val response = repository.getHistory(email, createdAt, title, score)

                if (response.predictions.isNullOrEmpty()) {
                    _historyLiveData.postValue(Result.Error("No Data")) // No data case
                    _noDataVisible.postValue(true)
                } else {
                    _historyLiveData.postValue(Result.Success(response))
                    _noDataVisible.postValue(false)
                }
            } catch (e: UnknownHostException) {
                // Masalah koneksi internet
                _historyLiveData.postValue(Result.Error("Error connection"))
            } catch (e: HttpException) {
                if (e.code() == 404) {
                    _historyLiveData.postValue(Result.Error("No Data")) // Tangani 404 sebagai No Data
                    _noDataVisible.postValue(true)
                } else {
                    _historyLiveData.postValue(Result.Error(e.message ?: "An error occurred."))
                    _noDataVisible.postValue(true)
                }
            } catch (e: IOException) {
                // Kesalahan jaringan lainnya
                _historyLiveData.postValue(Result.Error("Error connection"))
            } catch (e: Exception) {
                _historyLiveData.postValue(Result.Error(e.message ?: "An error occurred."))
                _noDataVisible.postValue(true)
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}
