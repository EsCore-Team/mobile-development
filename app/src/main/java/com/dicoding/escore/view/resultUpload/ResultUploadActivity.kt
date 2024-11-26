package com.dicoding.escore.view.resultUpload

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.escore.R
import com.dicoding.escore.databinding.ActivityResultUploadBinding
import com.dicoding.escore.databinding.ActivityUploadBinding
import com.dicoding.escore.view.main.MainActivity

class ResultUploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultUploadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}