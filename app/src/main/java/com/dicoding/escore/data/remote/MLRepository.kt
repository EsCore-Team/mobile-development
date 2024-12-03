package com.dicoding.escore.data.remote

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.dicoding.escore.R
import com.dicoding.escore.data.remote.response.ModelMachineLearningResponse
import com.dicoding.escore.data.remote.retrofit.ApiService
import com.dicoding.escore.databinding.ActivityUploadBinding
import com.dicoding.escore.pref.SessionManager
import com.dicoding.escore.view.ViewModelFactoryML
import com.dicoding.escore.view.upload.UploadViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.File

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

