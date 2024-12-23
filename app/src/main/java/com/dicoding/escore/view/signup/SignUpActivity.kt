package com.dicoding.escore.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.escore.R
import com.dicoding.escore.databinding.ActivitySignUpBinding
import com.dicoding.escore.view.ViewModelFactory
import com.dicoding.escore.view.login.LoginActivity
import com.dicoding.escore.data.remote.Result
import com.dicoding.escore.view.validation.EmailValidation
import com.dicoding.escore.view.validation.PasswordValidation

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding

    private val viewModel by viewModels<SignUpViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()

        binding.toLogin.setOnClickListener {
            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (isInputValid(name, email, password)) {
                viewModel.register(name, email, password)
            }
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.registerResult.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Success -> {
                    showLoading(false)
                    // Menampilkan pesan sukses registrasi
                    Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()

                    // Arahkan ke LoginActivity setelah registrasi berhasil
                    val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                is Result.Error -> {
                    showLoading(false)

                    // Mengecek jika error berhubungan dengan koneksi
                    val errorMessage = result.error ?: "Unknown error"
                    if (errorMessage.contains("Connection error", true) || errorMessage.contains("unable to resolve host", true)) {
                        // Tampilkan toast jika masalah koneksi
                        Toast.makeText(this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show()
                    } else if (errorMessage.contains("HTTP 400", true)) {
                        Toast.makeText(
                            this,
                            getString(R.string.email_already_exists),
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // Tampilkan pesan error lainnya
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }


    private fun isInputValid(fullName: String, email: String, password: String): Boolean {
        var isValid = true

        if (fullName.isEmpty()) {
            binding.nameEditTextLayout.error = getString(R.string.empty_name)
            isValid = false
        } else {
            binding.nameEditTextLayout.error = null
        }

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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE

    }

    private fun playAnimation() {

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(200)
        val nameText = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(200)
        val nameEditLayout = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(200)
        val emailTextView = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(200)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(200)
        val passwordTextView = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(200)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(200)
        val signupButton = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(200)
        val signIn = ObjectAnimator.ofFloat(binding.toSignInLayout, View.ALPHA, 1f).setDuration(200)

        AnimatorSet().apply {
            playSequentially(title, nameText, nameEditLayout, emailTextView, emailEditTextLayout, passwordTextView, passwordEditTextLayout, signupButton, signIn)
            start()
        }

    }
}