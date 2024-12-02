package com.dicoding.escore.view.resultUpload

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.escore.databinding.ActivityResultUploadBinding
import com.dicoding.escore.pref.SessionManager
import com.dicoding.escore.view.main.MainActivity

class ResultUploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultUploadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sessionManager = SessionManager(this)

        binding.btnReturnHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        val score = intent.getStringExtra("PREDICT_RESULT") ?: "Result not available"
        val suggestion = intent.getStringExtra("PREDICT_SUGGESTION") ?: "No suggestions available"
        val title = sessionManager.getEssayTitle() ?: "Title not available"
        val description = sessionManager.getEssayDescription() ?: "Description not available"

        binding.textResultUpload.text = score
        binding.suggestion.text = suggestion
        binding.tvEssayTitle.text = title
        binding.description.text = description
    }
}