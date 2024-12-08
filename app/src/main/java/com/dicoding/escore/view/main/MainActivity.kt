package com.dicoding.escore.view.main

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.dicoding.escore.R
import com.dicoding.escore.databinding.ActivityMainBinding
import com.dicoding.escore.pref.SessionManager
import com.dicoding.escore.view.onboarding.OnboardingActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)
        checkSession()

        // Bottom Navigation dan NavController
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navView.setupWithNavController(navController)
        // Atur warna ikon aktif dan tidak aktif
        setupBottomNavigationView(navView)

    }

    private fun setupBottomNavigationView(navView: BottomNavigationView) {
        // Warna aktif dan tidak aktif
        val colorStateList = ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked),  // State aktif
                intArrayOf(-android.R.attr.state_checked) // State tidak aktif
            ),
            intArrayOf(
                ContextCompat.getColor(this, R.color.white),  // Warna aktif
                ContextCompat.getColor(this, R.color.colorPrimary) // Warna tidak aktif
            )
        )

        // Terapkan warna ke ikon dan teks
        navView.itemIconTintList = colorStateList
        navView.itemTextColor = colorStateList

    }

    private fun checkSession() {
        val token = sessionManager.getAuthToken()
        if (token == null) {
            // Jika token tidak ada, navigasi ke layar login
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, OnboardingActivity::class.java)
        startActivity(intent)
        finish()
    }
}
