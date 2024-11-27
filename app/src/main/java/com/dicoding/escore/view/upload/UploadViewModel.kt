package com.dicoding.escore.view.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.escore.data.remote.MLRepository
import com.dicoding.escore.data.remote.Result
import com.dicoding.escore.data.remote.UserRepository
import com.dicoding.escore.data.remote.response.ModelMachineLearningResponse
import com.dicoding.escore.data.remote.response.SignUpResponse
import kotlinx.coroutines.launch

class UploadViewModel(private val repository: MLRepository): ViewModel()  {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _predictResult = MutableLiveData<Result<ModelMachineLearningResponse>>()
    val predictResult: LiveData<Result<ModelMachineLearningResponse>> = _predictResult

    fun predict(essay: String) {
        _isLoading.value = true
        viewModelScope.launch {
            val result = repository.predictML(essay)
            _predictResult.value = result
            _isLoading.value = false
        }
    }
}