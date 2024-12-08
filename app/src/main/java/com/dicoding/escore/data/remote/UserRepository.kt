package com.dicoding.escore.data.remote

import com.dicoding.escore.data.remote.response.HistoryResponse
import com.dicoding.escore.data.remote.response.LoginResponse
import com.dicoding.escore.data.remote.response.SignUpResponse
import com.dicoding.escore.data.remote.retrofit.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class UserRepository private constructor(
    private val apiService : ApiService
) {
    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.login(email, password)
                if (response.error == false) {
                    Result.Success(response)
                } else {
                    Result.Error(response.message)
                }
            } catch (e: Exception) {
                Result.Error("${e.message}")
            }
        }
    }

    suspend fun register(fullName: String, email: String, password: String): Result<SignUpResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.register(fullName, email, password)

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

    suspend fun getHistory(
        email: String,
        createdAt: String,
        title: String,
        score: String
    ): HistoryResponse {
        return apiService.history(
            email = email,
            createdAt = createdAt,
            title = title,
            score = score
        )
    }

    suspend fun getDetailHistory(email: String, id: String): HistoryResponse {
        return apiService.getDetailHistory(email, id)
    }

    companion object {
        @Volatile
        private var INSTANCE: UserRepository? = null
        fun getInstance(
            apiService: ApiService
        ): UserRepository = INSTANCE ?: synchronized(this) {
            INSTANCE ?: UserRepository(apiService)
        }.also { INSTANCE = it }
    }
}