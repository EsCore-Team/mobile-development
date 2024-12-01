package com.dicoding.escore.data.remote

import com.dicoding.escore.data.remote.response.ModelMachineLearningResponse
import com.dicoding.escore.data.remote.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class MLRepository private constructor(
    private val apiService : ApiService
) {
    suspend fun predictML(userEmail: String, title: String, essay: String): Result<ModelMachineLearningResponse>{
        return withContext(Dispatchers.IO){
            try {
                val response = apiService.predict(userEmail, title, essay)
                if (response.error == false) {
                    Result.Success(response)
                } else {
                    Result.Error(response.message)
                }
            } catch (e: HttpException) {
                Result.Error("${e.message}")
            }
        }
    }
    companion object {
        @Volatile
        private var INSTANCE: MLRepository? = null
        fun getInstance(
            apiService: ApiService
        ): MLRepository = INSTANCE ?: synchronized(this) {
            INSTANCE ?: MLRepository(apiService)
        }.also { INSTANCE = it }
    }
}