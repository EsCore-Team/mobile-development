package com.dicoding.escore.data.remote

import androidx.lifecycle.MediatorLiveData
import com.dicoding.escore.data.local.entity.HistoryEntity
import com.dicoding.escore.data.local.room.HistoryDao
import com.dicoding.escore.data.remote.response.HistoryResponse
import com.dicoding.escore.data.remote.response.LoginResponse
import com.dicoding.escore.data.remote.response.SignUpResponse
import com.dicoding.escore.data.remote.retrofit.ApiService
import com.dicoding.escore.utils.AppExecutors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class UserRepository private constructor(
    private val apiService : ApiService,
    private val historyDao: HistoryDao,
    private val appExecutors: AppExecutors
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

//    suspend fun getHistory(
//        email: String,
//        createdAt: String,
//        title: String,
//        score: String
//    ): HistoryResponse {
//        return apiService.history(
//            email = email,
//            createdAt = createdAt,
//            title = title,
//            score = score
//        )
//    }

    suspend fun getHistory(
        email: String,
        createdAt: String,
        title: String,
        score: String
    ): Result<HistoryResponse> {
        val result = MediatorLiveData<Result<List<HistoryEntity>>>()

        return withContext(Dispatchers.IO) {
            try {
                // Memanggil API untuk mendapatkan data riwayat
                val response = apiService.history(
                    email = email,
                    createdAt = createdAt,
                    title = title,
                    score = score
                )

                if (response.predictions.isNullOrEmpty()) {
                    Result.Error("No Data")
                } else {
                    // Opsional: Simpan data ke dalam database lokal
                    appExecutors.diskIO.execute {
                        val historyList = response.predictions.map {
                            HistoryEntity(
                                email = email,
                                createdAt = createdAt,
                                title = title,
                                score = score
                            )
                        }
                        historyDao.insertHistory(historyList)
                    }

                    // Mengambil data lokal dari newsDao
                    val localData = historyDao.getHistory()
                    result.addSource(localData) { newData: List<HistoryEntity> ->
                        result.value = Result.Success(newData)
                    }

                    Result.Success(response)
                }
            } catch (e: Exception) {
                Result.Error(e.message ?: "An error occurred.")
            }
        }
    }



    suspend fun getDetailHistory(email: String, id: String): HistoryResponse {
        return apiService.getDetailHistory(email, id)
    }

    companion object {
        @Volatile
        private var INSTANCE: UserRepository? = null
        fun getInstance(
            apiService: ApiService,
            historyDao: HistoryDao,
            appExecutors: AppExecutors
        ): UserRepository = INSTANCE ?: synchronized(this) {
            INSTANCE ?: UserRepository(apiService, historyDao, appExecutors)
        }.also { INSTANCE = it }
    }
}