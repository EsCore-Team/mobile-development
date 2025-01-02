package com.dicoding.escore.view.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.escore.data.local.entity.HistoryEntity
import com.dicoding.escore.data.remote.Result
import com.dicoding.escore.data.remote.UserRepository
import com.dicoding.escore.data.remote.response.HistoryResponse
import com.dicoding.escore.pref.SessionManager
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException

//
//class HistoryViewModel(
//    private val repository: UserRepository,
//    private val sessionManager: SessionManager
//) : ViewModel() {
//
//    private val _historyLiveData = MutableLiveData<Result<HistoryResponse>>()
//    val historyLiveData: LiveData<Result<HistoryResponse>> get() = _historyLiveData
//
//    private val _isLoading = MutableLiveData<Boolean>()
//    val isLoading: LiveData<Boolean> = _isLoading
//
//    // MutableLiveData untuk mengontrol visibilitas "No Data History"
//    private val _noDataVisible = MutableLiveData<Boolean>()
//    val noDataVisible: LiveData<Boolean> = _noDataVisible
//
//    fun fetchHistory(createdAt: String, title: String, score: String) {
//        viewModelScope.launch {
//            _historyLiveData.postValue(Result.Loading)
//            _isLoading.postValue(true)
//
//            try {
//                val email = sessionManager.getUserEmail()
//                if (email.isNullOrEmpty()) {
//                    _historyLiveData.postValue(Result.Error("Email not found in session."))
//                    _isLoading.postValue(false)
//                    _noDataVisible.postValue(true)
//                    return@launch
//                }
//
//                val response = repository.getHistory(email, createdAt, title, score)
//
//                if (response.predictions.isNullOrEmpty()) {
//                    _historyLiveData.postValue(Result.Error("No Data")) // No data case
//                    _noDataVisible.postValue(true)
//                } else {
//                    _historyLiveData.postValue(Result.Success(response))
//                    _noDataVisible.postValue(false)
//                }
//            } catch (e: UnknownHostException) {
//                // Masalah koneksi internet
//                _historyLiveData.postValue(Result.Error("Error connection"))
//            } catch (e: HttpException) {
//                if (e.code() == 404) {
//                    _historyLiveData.postValue(Result.Error("No Data")) // Tangani 404 sebagai No Data
//                    _noDataVisible.postValue(true)
//                } else {
//                    _historyLiveData.postValue(Result.Error(e.message ?: "An error occurred."))
//                    _noDataVisible.postValue(true)
//                }
//            } catch (e: IOException) {
//                // Kesalahan jaringan lainnya
//                _historyLiveData.postValue(Result.Error("Error connection"))
//            } catch (e: Exception) {
//                _historyLiveData.postValue(Result.Error(e.message ?: "An error occurred."))
//                _noDataVisible.postValue(true)
//            } finally {
//                _isLoading.postValue(false)
//            }
//        }
//    }
//}

// 2

//    class HistoryViewModel(
//        private val repository: UserRepository,
//        private val sessionManager: SessionManager
//    ) : ViewModel() {
//
//        private val _historyLiveData = MutableLiveData<Result<List<HistoryEntity>>>()
//        val historyLiveData: LiveData<Result<List<HistoryEntity>>> get() = _historyLiveData
//
//        private val _isLoading = MutableLiveData<Boolean>()
//        val isLoading: LiveData<Boolean> = _isLoading
//
//        // MutableLiveData untuk mengontrol visibilitas "No Data History"
//        private val _noDataVisible = MutableLiveData<Boolean>()
//        val noDataVisible: LiveData<Boolean> = _noDataVisible
//
//        fun fetchHistory(createdAt: String, title: String, score: String) {
//            viewModelScope.launch {
//                _isLoading.postValue(true)
//
//                try {
//                    val email = sessionManager.getUserEmail()
//                    if (email.isNullOrEmpty()) {
//                        _historyLiveData.postValue(Result.Error("Email not found in session."))
//                        _isLoading.postValue(false)
//                        _noDataVisible.postValue(true)
//                        return@launch
//                    }
//
//                    // Memanggil repository dan mengobservasi hasilnya
//                    val historyResult = repository.getHistory(email, createdAt, title, score)
//                    historyResult.observeForever { result ->
//                        when (result) {
//                            is Result.Loading -> {
//                                _historyLiveData.postValue(Result.Loading)
//                            }
//                            is Result.Success -> {
//                                if (result.data.isNullOrEmpty()) {
//                                    _historyLiveData.postValue(Result.Error("No Data"))
//                                    _noDataVisible.postValue(true)
//                                } else {
//                                    _historyLiveData.postValue(Result.Success(result.data))
//                                    _noDataVisible.postValue(false)
//                                }
//                            }
//                            is Result.Error -> {
//                                _historyLiveData.postValue(Result.Error(result.error))
//                                _noDataVisible.postValue(true)
//                            }
//                        }
//                        _isLoading.postValue(false)
//                    }
//                } catch (e: UnknownHostException) {
//                    _historyLiveData.postValue(Result.Error("Error connection"))
//                    _noDataVisible.postValue(true)
//                } catch (e: HttpException) {
//                    if (e.code() == 404) {
//                        _historyLiveData.postValue(Result.Error("No Data"))
//                        _noDataVisible.postValue(true)
//                    } else {
//                        _historyLiveData.postValue(Result.Error(e.message ?: "An error occurred."))
//                        _noDataVisible.postValue(true)
//                    }
//                } catch (e: IOException) {
//                    _historyLiveData.postValue(Result.Error("Error connection"))
//                    _noDataVisible.postValue(true)
//                } catch (e: Exception) {
//                    _historyLiveData.postValue(Result.Error(e.message ?: "An error occurred."))
//                    _noDataVisible.postValue(true)
//                } finally {
//                    _isLoading.postValue(false)
//                }
//            }
//        }
//    }

class HistoryViewModel(
    private val repository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _historyLiveData = MutableLiveData<Result<List<HistoryEntity>>>()
    val historyLiveData: LiveData<Result<List<HistoryEntity>>> get() = _historyLiveData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _noDataVisible = MutableLiveData<Boolean>()
    val noDataVisible: LiveData<Boolean> = _noDataVisible


    fun fetchHistory(createdAt: String, title: String, score: String) {
        viewModelScope.launch {
            _isLoading.value = true

            try {
                val email = sessionManager.getUserEmail()
                if (email.isNullOrEmpty()) {
                    _historyLiveData.value = Result.Error("Email not found in session.")
                    _isLoading.value = false
                    _noDataVisible.value = true
                    return@launch
                }

                // Panggil repository dan dapatkan data history
                val historyResult = repository.getHistory(email, createdAt, title, score)

                // Observasi hasil dari repository
                historyResult.observeForever { result ->
                    when (result) {
                        is Result.Loading -> _isLoading.value = true
                        is Result.Success -> {
                            _historyLiveData.value = Result.Success(result.data)
                            _noDataVisible.value = result.data.isNullOrEmpty()
                            _isLoading.value = false
                        }
                        is Result.Error -> {
                            _historyLiveData.value = Result.Error(result.error)
                            _noDataVisible.value = true
                            _isLoading.value = false
                        }
                    }
                }
            } catch (e: Exception) {
                _historyLiveData.value = Result.Error(e.message ?: "An error occurred.")
                _noDataVisible.value = true
                _isLoading.value = false
            }
        }
    }

}


