package com.dicoding.escore.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.escore.data.remote.MLRepository
import com.dicoding.escore.data.remote.UserRepository
import com.dicoding.escore.di.Injection
import com.dicoding.escore.di.InjectionML
import com.dicoding.escore.view.login.LoginViewModel
import com.dicoding.escore.view.main.MainViewModel
import com.dicoding.escore.view.signup.SignUpViewModel
import com.dicoding.escore.view.upload.UploadViewModel

class ViewModelFactoryML(private val repository: MLRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(UploadViewModel::class.java) -> {
                UploadViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactoryML? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactoryML {
            if (INSTANCE == null) {
                synchronized(ViewModelFactoryML::class.java) {
                    INSTANCE = ViewModelFactoryML(InjectionML.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactoryML
        }
    }
}