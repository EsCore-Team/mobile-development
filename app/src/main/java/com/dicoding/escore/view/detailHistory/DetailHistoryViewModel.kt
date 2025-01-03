package com.dicoding.escore.view.detailHistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.escore.data.remote.Result
import com.dicoding.escore.data.remote.UserRepository
import com.dicoding.escore.data.remote.response.PredictionsItem
import com.dicoding.escore.pref.SessionManager
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.UnknownHostException

class DetailHistoryViewModel(
    private val repository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

//    private val _detailLiveData = MutableLiveData<PredictionsItem?>()
//    val detailLiveData: MutableLiveData<PredictionsItem?> get() = _detailLiveData

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _noDataVisible = MutableLiveData<Boolean>()
    val noDataVisible: LiveData<Boolean> = _noDataVisible

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorLiveData

    fun getUserEmail(): String? {
        return sessionManager.getUserEmail()
    }

//    fun fetchDetailHistory(email: String, id: String) {
//        viewModelScope.launch {
//            try {
//                val response = repository.getDetailHistory(email, id)
//                val prediction = response.predictions?.find { it?.id == id }
//                if (prediction != null) {
//                    _detailLiveData.postValue(prediction)
//                } else {
//                    _errorLiveData.postValue("Detail not found.")
//                }
//            } catch (e: Exception) {
//                _errorLiveData.postValue(e.message ?: "An error occurred.")
//            }
//        }
//    }

    private val _detailLiveData = MutableLiveData<Result<PredictionsItem>>()
    val detailLiveData: LiveData<Result<PredictionsItem>> = _detailLiveData

    fun fetchDetailHistory(email: String, id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _detailLiveData.value = Result.Loading // Status loading ditandai di awal

            try {
                val response = repository.getDetailHistory(email, id)
                val prediction = response.predictions?.find { it?.id == id }

                if (prediction != null) {
                    _detailLiveData.value = Result.Success(prediction)
                    _noDataVisible.value = false
                } else {
                    _detailLiveData.value = Result.Error("Detail not found.")
                    _noDataVisible.value = true
                }
            } catch (e: UnknownHostException) {
                _detailLiveData.value = Result.Error("Error connection")
                _noDataVisible.value = true
            } catch (e: HttpException) {
                if (e.code() == 404) {
                    _detailLiveData.value = Result.Error("No Data")
                    _noDataVisible.value = true
                } else {
                    _detailLiveData.value = Result.Error(e.message ?: "An error occurred.")
                    _noDataVisible.value = true
                }
            } catch (e: IOException) {
                _detailLiveData.value = Result.Error("Error connection")
                _noDataVisible.value = true
            } catch (e: Exception) {
                _detailLiveData.value = Result.Error(e.message ?: "An error occurred.")
                _noDataVisible.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }


}
