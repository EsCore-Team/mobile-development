package com.dicoding.escore.pref

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val preferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "user_session"
        private const val TOKEN_KEY = "auth_token"
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
}
