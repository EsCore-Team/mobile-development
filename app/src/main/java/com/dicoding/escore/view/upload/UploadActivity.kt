package com.dicoding.escore.view.upload

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.dicoding.escore.R
import com.dicoding.escore.data.remote.Result
import com.dicoding.escore.databinding.ActivityUploadBinding
import com.dicoding.escore.pref.SessionManager
import com.dicoding.escore.view.ViewModelFactoryML
import com.dicoding.escore.view.resultUpload.ResultUploadActivity

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding

    private val viewModel by viewModels<UploadViewModel> {
        ViewModelFactoryML.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi SessionManager
        val sessionManager = SessionManager(this)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar) // Set Toolbar as the ActionBar
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.submitButton.setOnClickListener {

            val userEmail = sessionManager.getUserEmail()
            val textTitle = binding.titleTextBox.text.toString().trim()
            val textDesc = binding.descTextBox.text.toString().trim()

            // Panggil fungsi ViewModel dengan email, title, dan desc
            if (userEmail != null) {
                viewModel.predict(userEmail, textTitle, textDesc)
            }   else {
                // Tampilkan pesan error jika email tidak ditemukan
                Toast.makeText(this, "Email tidak ditemukan. Silakan login ulang.", Toast.LENGTH_SHORT).show()
            }

        }

        observeViewModel()
    }


    private fun observeViewModel() {
        val sessionManager = SessionManager(this) // Inisialisasi SessionManager

        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.predictResult.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Success -> {
                    showLoading(false)
                    val score = result.data.result?.predictedResult?.score ?: 0 // Nilai default jika null
                    val suggestion = result.data.result?.predictedResult?.suggestion ?: "Tidak ada saran" // Nilai default jika null
                    val title = result.data.result?.title ?: "Judul tidak tersedia" // Nilai default jika null
                    val description = result.data.result?.essay ?: "Deskripsi tidak tersedia" // Nilai default jika null

                    // Simpan title dan description ke SessionManager
                    sessionManager.saveEssayTitle(title)
                    sessionManager.saveEssayDescription(description)

                    Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()

                    val intent = Intent(this@UploadActivity, ResultUploadActivity::class.java).apply {
                        putExtra("PREDICT_RESULT", score.toString()) // Kirim nilai score
                        putExtra("PREDICT_SUGGESTION", suggestion) // Kirim nilai suggestion
                    }
                    startActivity(intent)
                    finish()
                }
                is Result.Error -> {
                    showLoading(false)
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE

    }
}