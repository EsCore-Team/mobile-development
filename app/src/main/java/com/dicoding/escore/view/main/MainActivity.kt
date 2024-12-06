package com.dicoding.escore.view.main

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
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

//class MainActivity : AppCompatActivity() {
//    private val viewModel by viewModels<MainViewModel> {
//        ViewModelFactory.getInstance(this)
//    }
//
//    private lateinit var binding: ActivityMainBinding
//    private lateinit var sessionManager: SessionManager
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        sessionManager = SessionManager(this)
//
//        checkSession()
////
////        setupAction()
//
//        val toolbar: Toolbar = findViewById(R.id.toolbar)
//        setSupportActionBar(toolbar) // Set Toolbar as the ActionBar
//        supportActionBar?.setDisplayShowTitleEnabled(false)
//
//        val navView: BottomNavigationView = binding.navView
//
//        val navController = findNavController(R.id.nav_host_fragment_activity_main)
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_guide, R.id.navigation_home, R.id.navigation_profile
//            )
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
//    }
//
//    private fun checkSession() {
//        val token = sessionManager.getAuthToken()
//        if (token == null) {
//            // Jika token tidak ada, navigasi ke layar login
//            navigateToLogin()
//        }
//    }
//    private fun navigateToLogin() {
//        val intent = Intent(this, OnboardingActivity::class.java)
//        startActivity(intent)
//        finish()
//    }
//}

//class MainActivity : AppCompatActivity() {
//    private val viewModel by viewModels<MainViewModel> {
//        ViewModelFactory.getInstance(this)
//    }
//
//    private lateinit var binding: ActivityMainBinding
//    private lateinit var sessionManager: SessionManager
//    private lateinit var toolbarTitle: TextView
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        sessionManager = SessionManager(this)
//        checkSession()
//
////        // Atur Toolbar
////        val toolbar: Toolbar = findViewById(R.id.toolbar)
////        setSupportActionBar(toolbar)
////        supportActionBar?.setDisplayShowTitleEnabled(false) // Hapus judul bawaan
////
////        // Tambahkan TextView untuk judul di tengah
////        toolbarTitle = TextView(this).apply {
////            textSize = 18f
////            setTextColor(Color.parseColor("#7AB2D3"))
////            typeface = Typeface.DEFAULT_BOLD
////            gravity = Gravity.CENTER
////        }
////
////        val layoutParams = Toolbar.LayoutParams(
////            Toolbar.LayoutParams.WRAP_CONTENT,
////            Toolbar.LayoutParams.WRAP_CONTENT
////        ).apply {
////            gravity = Gravity.CENTER
////        }
////        toolbar.addView(toolbarTitle, layoutParams)
//
//        // Bottom Navigation dan NavController
//        val navView: BottomNavigationView = binding.navView
//        val navController = findNavController(R.id.nav_host_fragment_activity_main)
//
//        // Konfigurasikan AppBar
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_guide, R.id.navigation_home, R.id.navigation_profile
//            )
//        )
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        navView.setupWithNavController(navController)
//
////        // Perbarui judul toolbar berdasarkan navigasi
////        navController.addOnDestinationChangedListener { _, destination, _ ->
////            toolbarTitle.text = destination.label
////        }
//    }
//
//    private fun checkSession() {
//        val token = sessionManager.getAuthToken()
//        if (token == null) {
//            // Jika token tidak ada, navigasi ke layar login
//            navigateToLogin()
//        }
//    }
//
//    private fun navigateToLogin() {
//        val intent = Intent(this, OnboardingActivity::class.java)
//        startActivity(intent)
//        finish()
//    }
//}

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

        // Bottom Navigation dan NavController
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navView.setupWithNavController(navController)

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
