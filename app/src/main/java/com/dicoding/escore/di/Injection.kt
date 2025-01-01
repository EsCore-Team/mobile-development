package com.dicoding.escore.di

import android.content.Context
import com.dicoding.escore.data.local.room.HistoryDatabase
import com.dicoding.escore.data.remote.UserRepository
import com.dicoding.escore.data.remote.retrofit.ApiConfig
import com.dicoding.escore.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(apiService)
    }
}