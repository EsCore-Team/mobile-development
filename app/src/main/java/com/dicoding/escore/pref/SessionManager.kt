package com.dicoding.escore.pref

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val preferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "user_session"
        private const val TOKEN_KEY = "auth_token"
        private const val EMAIL_KEY = "user_email" // Tambahkan key untuk email
        private const val FULLNAME_KEY = "user_fullname" // Key untuk fullName
    }
    fun saveAuthToken(token: String) {
        preferences.edit().putString(TOKEN_KEY, token).apply()
    }

    fun getAuthToken(): String? {
        return preferences.getString(TOKEN_KEY, null)
    }

    fun clearAuthToken() {
        preferences.edit().remove(TOKEN_KEY).apply()
    }

    // Simpan email
    fun saveUserEmail(email: String) {
        preferences.edit().putString(EMAIL_KEY, email).apply()
    }

    // Ambil email
    fun getUserEmail(): String? {
        return preferences.getString(EMAIL_KEY, null)
    }

    // Hapus email
    fun clearUserEmail() {
        preferences.edit().remove(EMAIL_KEY).apply()
    }

    fun saveUserFullName(fullName: String) {
        preferences.edit().putString(FULLNAME_KEY, fullName).apply()
    }

    // Ambil fullName
    fun getUserFullName(): String? {
        return preferences.getString(FULLNAME_KEY, null)
    }
}
