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
import com.dicoding.escore.pref.SessionManager
import com.dicoding.escore.view.main.MainActivity
import com.dicoding.escore.view.upload.UploadActivity

//class ResultUploadActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityResultUploadBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityResultUploadBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//    }
//
//    override fun onBackPressed() {
//        super.onBackPressed()
//        val intent = Intent(this, MainActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
//        startActivity(intent)
//        finish()
//    }
//}

class ResultUploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultUploadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sessionManager = SessionManager(this) // Inisialisasi SessionManager

        binding.btnReturnHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        // Ambil hasil score dan suggestion dari intent
        val score = intent.getStringExtra("PREDICT_RESULT") ?: "Hasil tidak tersedia"
        val suggestion = intent.getStringExtra("PREDICT_SUGGESTION") ?: "Tidak ada saran"

        // Ambil title dan description dari SessionManager
        val title = sessionManager.getEssayTitle() ?: "Judul tidak tersedia"
        val description = sessionManager.getEssayDescription() ?: "Deskripsi tidak tersedia"

        // Tampilkan hasil pada TextView
        binding.textResultUpload.text = score
        binding.suggestion.text = suggestion
        binding.tvEssayTitle.text = title
        binding.description.text = description // Tambahkan ini untuk menampilkan deskripsi
    }



    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}
