package com.dicoding.escore.view.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.escore.data.remote.MLRepository
import com.dicoding.escore.data.remote.Result
import com.dicoding.escore.data.remote.response.ModelMachineLearningResponse
import kotlinx.coroutines.launch
import java.io.IOException

class UploadViewModel(private val repository: MLRepository): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _predictResult = MutableLiveData<Result<ModelMachineLearningResponse>>()
    val predictResult: LiveData<Result<ModelMachineLearningResponse>> = _predictResult

    fun predict(userEmail: String, title: String, essay: String) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = repository.predictML(userEmail, title, essay)
                _predictResult.value = result
            } catch (e: IOException) {
                // Tangani kesalahan koneksi
                _predictResult.value = Result.Error("Error connection. Please check your internet connection.")
            } catch (e: Exception) {
                // Tangani kesalahan lain
                _predictResult.value = Result.Error("An unexpected error occurred: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }
}
