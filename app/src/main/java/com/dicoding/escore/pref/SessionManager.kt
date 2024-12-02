package com.dicoding.escore.pref

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val preferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "user_session"
        private const val TOKEN_KEY = "auth_token"
        private const val EMAIL_KEY = "user_email"
        private const val FULLNAME_KEY = "user_fullname"
        private const val TITLE_KEY = "essay_title" // Key untuk title
        private const val DESCRIPTION_KEY = "essay_description" // Key untuk description
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

    // Simpan full name
    fun saveUserFullName(fullName: String) {
        preferences.edit().putString(FULLNAME_KEY, fullName).apply()
    }

    // Ambil full name
    fun getUserFullName(): String? {
        return preferences.getString(FULLNAME_KEY, null)
    }

    // Simpan title
    fun saveEssayTitle(title: String) {
        preferences.edit().putString(TITLE_KEY, title).apply()
    }

    // Ambil title
    fun getEssayTitle(): String? {
        return preferences.getString(TITLE_KEY, null)
    }

    // Hapus title
    fun clearEssayTitle() {
        preferences.edit().remove(TITLE_KEY).apply()
    }

    // Simpan description
    fun saveEssayDescription(description: String) {
        preferences.edit().putString(DESCRIPTION_KEY, description).apply()
    }

    // Ambil description
    fun getEssayDescription(): String? {
        return preferences.getString(DESCRIPTION_KEY, null)
    }

    // Hapus description
    fun clearEssayDescription() {
        preferences.edit().remove(DESCRIPTION_KEY).apply()
    }
}
