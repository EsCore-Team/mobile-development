package com.dicoding.escore.view

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.escore.data.remote.UserRepository
import com.dicoding.escore.di.Injection
import com.dicoding.escore.pref.SessionManager
import com.dicoding.escore.view.bottombar.home.HomeViewModel
import com.dicoding.escore.view.bottombar.profile.ProfileViewModel
import com.dicoding.escore.view.detailHistory.DetailHistoryViewModel
import com.dicoding.escore.view.history.HistoryViewModel
import com.dicoding.escore.view.login.LoginViewModel
import com.dicoding.escore.view.main.MainViewModel
import com.dicoding.escore.view.signup.SignUpViewModel
import com.dicoding.escore.view.upload.UploadViewModel

//class ViewModelFactory(private val repository: UserRepository) : ViewModelProvider.NewInstanceFactory() {
//
//    @Suppress("UNCHECKED_CAST")
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        return when {
//            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
//                MainViewModel(repository) as T
//            }
//            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
//                LoginViewModel(repository) as T
//            }
//            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
//                SignUpViewModel(repository) as T
//            }            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
//                SignUpViewModel(repository) as T
//            }
//
//            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
//        }
//    }
//
//    companion object {
//        @Volatile
//        private var INSTANCE: ViewModelFactory? = null
//        @JvmStatic
//        fun getInstance(context: Context): ViewModelFactory {
//            if (INSTANCE == null) {
//                synchronized(ViewModelFactory::class.java) {
//                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
//                }
//            }
//            return INSTANCE as ViewModelFactory
//        }
//    }
//}

class ViewModelFactory(
    private val repository: UserRepository,
    private val sessionManager: SessionManager
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(sessionManager) as T
            }
            modelClass.isAssignableFrom(HistoryViewModel::class.java) -> {
                HistoryViewModel(repository, sessionManager) as T
            }
            modelClass.isAssignableFrom(DetailHistoryViewModel::class.java) -> {
                DetailHistoryViewModel(repository, sessionManager) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository, sessionManager) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    val sessionManager = SessionManager(context)
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context), sessionManager)
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}
