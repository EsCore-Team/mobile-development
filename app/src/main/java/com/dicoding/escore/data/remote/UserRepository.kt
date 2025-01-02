package com.dicoding.escore.data.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.liveData
import com.dicoding.escore.BuildConfig
import com.dicoding.escore.data.local.entity.HistoryEntity
import com.dicoding.escore.data.local.room.HistoryDao
import com.dicoding.escore.data.local.room.HistoryDatabase
import com.dicoding.escore.data.remote.response.HistoryResponse
import com.dicoding.escore.data.remote.response.LoginResponse
import com.dicoding.escore.data.remote.response.SignUpResponse
import com.dicoding.escore.data.remote.retrofit.ApiService
import com.dicoding.escore.utils.AppExecutors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.util.ArrayList

class UserRepository private constructor(
    private val apiService : ApiService,
    private val historyDao: HistoryDao,
    private val appExecutors: AppExecutors

) {
    private val result = MediatorLiveData<Result<List<HistoryEntity>>>()

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

//    suspend fun getHistory(email: String, createdAt: String, title: String, score: String): LiveData<Result<List<HistoryEntity>>> {
//        result.value = Result.Loading
//        try {
//            // Hapus data lokal terkait email sebelum menyimpan data baru
//            appExecutors.diskIO.execute {
//                historyDao.deleteByEmail(email)
//            }
//
//            // Pemanggilan API langsung
//            val response = apiService.history(email, createdAt, title, score)
//
//            // Jika respons berhasil
//            val historyList = ArrayList<HistoryEntity>()
//            response.predictions?.forEach { history ->
//                val historyEntity = HistoryEntity(
//                    id = history?.id ?: "",
//                    email = email, // Pastikan email juga disimpan di database
//                    createdAt = history?.createdAt,
//                    title = history?.title,
//                    score = history?.predictedResult?.score
//                )
//                historyList.add(historyEntity)
//            }
//
//            // Simpan data ke database lokal
//            appExecutors.diskIO.execute {
//                historyDao.insert(historyList)
//            }
//
//            // Ambil data lokal terbaru dari database dan atur status hasil ke sukses
//            val localData = historyDao.getAllHistory(email) // Filter berdasarkan email
//            result.addSource(localData) { newData: List<HistoryEntity> ->
//                result.value = Result.Success(newData)
//            }
//        } catch (e: Exception) {
//            // Tangani kesalahan
//            result.value = Result.Error(e.message.toString())
//        }
//
//        return result
//    }

    suspend fun getHistory(email: String, createdAt: String, title: String, score: String): LiveData<Result<List<HistoryEntity>>> {
        result.value = Result.Loading
        try {
            // Ambil data dari database lokal
            val localData = historyDao.getAllHistory(email) // Ambil data terkait email
            result.addSource(localData) { newData: List<HistoryEntity> ->
                result.value = if (newData.isNotEmpty()) {
                    Result.Success(newData)
                } else {
                    Result.Error("No Data")
                }
            }

            // Coba ambil data dari API jika koneksi tersedia
            val response = apiService.history(email, createdAt, title, score)

            // Jika respons berhasil
            val historyList = response.predictions?.map { history ->
                HistoryEntity(
                    id = history?.id ?: "",
                    email = email,
                    createdAt = history?.createdAt,
                    title = history?.title,
                    score = history?.predictedResult?.score
                )
            } ?: emptyList()

            // Perbarui database lokal
            appExecutors.diskIO.execute {
                historyDao.deleteByEmail(email)
                historyDao.insert(historyList)
            }

            // Tambahkan data baru ke LiveData
            result.addSource(historyDao.getAllHistory(email)) { updatedData ->
                result.value = Result.Success(updatedData)
            }
        } catch (e: Exception) {
            // Tangani kesalahan koneksi, tetap tampilkan data lokal
            result.value = Result.Error(e.message.toString())
        }

        return result
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