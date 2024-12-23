package com.dicoding.escore.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.dicoding.escore.R
import com.dicoding.escore.databinding.ActivityLoginBinding
import com.dicoding.escore.pref.SessionManager
import com.dicoding.escore.view.ViewModelFactory
import com.dicoding.escore.data.remote.Result
import com.dicoding.escore.view.main.MainActivity
import com.dicoding.escore.view.signup.SignUpActivity
import com.dicoding.escore.view.validation.EmailValidation
import com.dicoding.escore.view.validation.PasswordValidation
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)


        sessionManager = SessionManager(this)

        binding.loginButton.setOnClickListener{
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (isInputValid(email, password)) {
                viewModel.login(email, password)
            }
        }
        playAnimation()

        binding.toSignup.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.loginResult.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Success -> {
                    showLoading(false)
                    val token = result.data.loginResult.token
                    val email = result.data.loginResult.email ?: "Email tidak tersedia"
                    val fullName = result.data.loginResult.fullName ?: "Nama tidak tersedia"
                    lifecycleScope.launch {
                        sessionManager.saveAuthToken(token)
                        sessionManager.saveUserEmail(email)
                        sessionManager.saveUserFullName(fullName)
                    }
                    Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()
                    navigateToMainActivity()
                }
                is Result.Error -> {
                    showLoading(false)
                    val errorMessage = result.error ?: "Unknown error"
                    if (errorMessage.contains("Connection error", true) || errorMessage.contains("unable to resolve host", true)) {
                        // Tampilkan toast jika ada masalah koneksi atau masalah host
                        Toast.makeText(this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show()
                    } else if (errorMessage.contains("Email and password not found", true)|| errorMessage.contains("HTTP 401", true)|| errorMessage.contains("HTTP 400", true)) {
                        // Tampilkan error lainnya (termasuk "Email and password not found")
                        Toast.makeText(this, getString(R.string.password_email_not_found), Toast.LENGTH_SHORT).show()
                    } else {
                        // Tampilkan error lainnya (termasuk "Email and password not found")
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE

    }

    private fun isInputValid(email: String, password: String): Boolean {
        var isValid = true

        val emailValidation = binding.emailEditText as? EmailValidation
        if (email.isEmpty()) {
            binding.emailEditTextLayout.error = getString(R.string.empty_email)
            isValid = false
        } else if ((emailValidation == null || !emailValidation.validateEmail(email))) {
            isValid = false
        } else {
            binding.emailEditTextLayout.error = null
        }

        val passwordValidation = binding.passwordEditText as? PasswordValidation
        val isPasswordValid = passwordValidation?.validatePassword(password) ?: false
        if (password.isEmpty()) {
            binding.passwordEditTextLayout.error = getString(R.string.empty_password)
            isValid = false
        } else if(!isPasswordValid){
            isValid = false
        }else {
            binding.passwordEditTextLayout.error = null
        }
        return isValid
    }


    private fun playAnimation() {

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(200)
        val emailTextView = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(200)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(200)
        val passwordTextView = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(200)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(200)
        val loginButton = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(200)
        val signUp = ObjectAnimator.ofFloat(binding.toSignupLayout, View.ALPHA, 1f).setDuration(200)

        AnimatorSet().apply {
            playSequentially(title, emailTextView, emailEditTextLayout, passwordTextView, passwordEditTextLayout, loginButton, signUp)
            start()
        }

    }

}