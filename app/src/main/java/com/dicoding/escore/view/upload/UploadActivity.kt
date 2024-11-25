package com.dicoding.escore.view.upload

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.escore.R
import com.dicoding.escore.databinding.ActivitySignUpBinding
import com.dicoding.escore.databinding.ActivityUploadBinding
import com.dicoding.escore.view.login.LoginActivity
import com.dicoding.escore.view.resultUpload.ResultUploadActivity
import com.dicoding.escore.view.signup.SignUpActivity

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.submitButton.setOnClickListener {
            val intent = Intent(this@UploadActivity, ResultUploadActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}