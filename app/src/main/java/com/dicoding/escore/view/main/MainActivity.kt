package com.dicoding.escore.view.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.dicoding.escore.R
import com.dicoding.escore.databinding.ActivityMainBinding
import com.dicoding.escore.pref.SessionManager
import com.dicoding.escore.view.ViewModelFactory
import com.dicoding.escore.view.onboarding.OnboardingActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(this)

        checkSession()
//
//        setupAction()

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar) // Set Toolbar as the ActionBar
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_guide, R.id.navigation_home, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun checkSession() {
        val token = sessionManager.getAuthToken()
        if (token == null) {
            // Jika token tidak ada, navigasi ke layar login
            navigateToLogin()
        }
    }
//
//    private fun setupAction() {
//        binding.logoutButton.setOnClickListener {
//            showLogoutConfirmation()
//            true
//        }
//    }
//
//    private fun showLogoutConfirmation() {
//        AlertDialog.Builder(this)
//            .setTitle(getString(R.string.confirm_logout))
//            .setMessage(getString(R.string.are_you_sure_you_want_to_logout))
//            .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
//                logout()
//                dialog.dismiss()
//            }
//            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
//                dialog.dismiss()
//            }
//            .show()
//    }
//
//    private fun logout() {
//        sessionManager.clearAuthToken()
//        navigateToLogin()
//    }
//
    private fun navigateToLogin() {
        val intent = Intent(this, OnboardingActivity::class.java)
        startActivity(intent)
        finish()
    }
}