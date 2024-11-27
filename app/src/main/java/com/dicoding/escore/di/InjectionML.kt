package com.dicoding.escore.di

import android.content.Context
import com.dicoding.escore.data.remote.MLRepository
import com.dicoding.escore.data.remote.UserRepository
import com.dicoding.escore.data.remote.retrofit.ApiConfig
import com.dicoding.escore.data.remote.retrofit.ApiConfigML

object InjectionML {
    fun provideRepository(context: Context): MLRepository {
        val apiService = ApiConfigML.getApiService()
        return MLRepository.getInstance(apiService)
    }
}