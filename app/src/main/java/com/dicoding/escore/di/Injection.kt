package com.dicoding.escore.di

import android.content.Context
import com.dicoding.escore.data.local.room.HistoryDatabase
import com.dicoding.escore.data.remote.UserRepository
import com.dicoding.escore.data.remote.retrofit.ApiConfig
import com.dicoding.escore.utils.AppExecutors

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val apiService = ApiConfig.getApiService()
        val database = HistoryDatabase.getInstance(context)
        val dao = database.historyDao()
        val appExecutors = AppExecutors()
        return UserRepository.getInstance(apiService, dao, appExecutors)
    }
}