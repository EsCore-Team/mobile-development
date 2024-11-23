package com.dicoding.escore.di

import android.content.Context
import com.dicoding.escore.data.remote.UserRepository
import com.dicoding.escore.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService)
    }
}