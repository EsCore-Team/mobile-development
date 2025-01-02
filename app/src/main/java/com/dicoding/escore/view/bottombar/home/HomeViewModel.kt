package com.dicoding.escore.view.bottombar.home

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

class HomeViewModel(
    private val repository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

//    private val _historyLiveData = MutableLiveData<Result<HistoryResponse>>()
//    val historyLiveData: LiveData<Result<HistoryResponse>> get() = _historyLiveData

    private val _historyLiveData = MutableLiveData<Result<List<HistoryEntity>>>()
    val historyLiveData: LiveData<Result<List<HistoryEntity>>> get() = _historyLiveData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _fullName = MutableLiveData<String>().apply {
        value = sessionManager.getUserFullName() ?: "Nama lengkap tidak tersedia"
    }
    val fullName: LiveData<String> = _fullName

    // MutableLiveData untuk mengontrol visibilitas "No Data History"
    private val _noDataVisible = MutableLiveData<Boolean>()
    val noDataVisible: LiveData<Boolean> = _noDataVisible

//    fun fetchHistory(createdAt: String, title: String, score: String) {
//        viewModelScope.launch {
//            _isLoading.value = true
//
//            try {
//                val email = sessionManager.getUserEmail()
//                if (email.isNullOrEmpty()) {
//                    _historyLiveData.value = Result.Error("Email not found in session.")
//                    _isLoading.value = false
//                    _noDataVisible.value = true
//                    return@launch
//                }
//
//                // Panggil repository dan dapatkan data history
//                val historyResult = repository.getHistory(email, createdAt, title, score)
//
//                // Observasi hasil dari repository
//                historyResult.observeForever { result ->
//                    when (result) {
//                        is Result.Loading -> {
//                            _historyLiveData.value = Result.Loading
//                        }
//                        is Result.Success -> {
//                            if (result.data.isNullOrEmpty()) {
//                                _historyLiveData.value = Result.Error("No Data")
//                                _noDataVisible.value = true
//                            } else {
//                                _historyLiveData.value = Result.Success(result.data)
//                                _noDataVisible.value = false
//                            }
//                        }
//                        is Result.Error -> {
//                            _historyLiveData.value = Result.Error(result.error)
//                            _noDataVisible.value = true
//                        }
//                    }
//                }
//            } catch (e: UnknownHostException) {
//                _historyLiveData.value = Result.Error("Error connection")
//                _noDataVisible.value = true
//            } catch (e: HttpException) {
//                if (e.code() == 404) {
//                    _historyLiveData.value = Result.Error("No Data")
//                    _noDataVisible.value = true
//                } else {
//                    _historyLiveData.value = Result.Error(e.message ?: "An error occurred.")
//                    _noDataVisible.value = true
//                }
//            } catch (e: IOException) {
//                _historyLiveData.value = Result.Error("Error connection")
//                _noDataVisible.value = true
//            } catch (e: Exception) {
//                _historyLiveData.value = Result.Error(e.message ?: "An error occurred.")
//                _noDataVisible.value = true
//            } finally {
//                _isLoading.value = false
//            }
//        }
//    }

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
            catch (e: UnknownHostException) {
                _historyLiveData.value = Result.Error("Error connection")
                _noDataVisible.value = true
            } catch (e: HttpException) {
                if (e.code() == 404) {
                    _historyLiveData.value = Result.Error("No Data")
                    _noDataVisible.value = true
                } else {
                    _historyLiveData.value = Result.Error(e.message ?: "An error occurred.")
                    _noDataVisible.value = true
                }
            } catch (e: IOException) {
                _historyLiveData.value = Result.Error("Error connection")
                _noDataVisible.value = true
            } catch (e: Exception) {
                _historyLiveData.value = Result.Error(e.message ?: "An error occurred.")
                _noDataVisible.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }

}

