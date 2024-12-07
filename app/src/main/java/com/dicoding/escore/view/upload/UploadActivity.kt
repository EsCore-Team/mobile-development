package com.dicoding.escore.view.upload

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.dicoding.escore.R
import com.dicoding.escore.data.remote.Result
import com.dicoding.escore.data.remote.response.ModelMachineLearningResponse
import com.dicoding.escore.databinding.ActivityUploadBinding
import com.dicoding.escore.pref.SessionManager
import com.dicoding.escore.view.ViewModelFactoryML
import com.dicoding.escore.view.resultUpload.ResultUploadActivity
import java.io.File


class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding

    private val viewModel by viewModels<UploadViewModel> {
        ViewModelFactoryML.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupWordCount()

        // Inisialisasi SessionManager
        val sessionManager = SessionManager(this)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar) // Set Toolbar as the ActionBar
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbar.navigationIcon?.setTint(getColor(R.color.black))


        binding.toolbar.setNavigationOnClickListener {
            onBackPressed() // Aksi tombol back
        }

        binding.submitButton.setOnClickListener {

            val userEmail = sessionManager.getUserEmail()
            val textTitle = binding.titleTextBox.text.toString().trim()
            val textDesc = binding.descTextBox.text.toString().trim()

            val wordCount = textDesc.split("\\s+".toRegex()).filter { it.isNotEmpty() }.size

            if (wordCount > 1000) {
                Toast.makeText(this, "Your essay exceeds the 1000 word limit!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Panggil fungsi ViewModel dengan email, title, dan desc
            if (isInputValid(textTitle, textDesc) && userEmail != null) {
                viewModel.predict(userEmail, textTitle, textDesc)
            } else{
                // Tampilkan pesan error jika email tidak ditemukan
                Toast.makeText(this, "Please complete all required fields!", Toast.LENGTH_SHORT).show()
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
                    val suggestion = result.data.result?.predictedResult?.suggestion ?: "No suggestions" // Nilai default jika null
                    val title = result.data.result?.title ?: "Title not available" // Nilai default jika null
                    val description = result.data.result?.essay ?: "Description not available" // Nilai default jika null

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
                    val errorMessage = result.error ?: "Unknown error"

                    if (errorMessage.contains("Error connection", true)) {
                        // Tampilkan pesan error jika ada masalah koneksi
                        Toast.makeText(this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show()
                    } else {
                        // Tampilkan pesan error lainnya
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun isInputValid(title: String, description: String): Boolean {
        var isValid = true
        val maxWords = 10

        // Hitung jumlah kata dalam title
        val wordCount = title.trim().split("\\s+".toRegex()).size

        // Validasi untuk title
        if (title.isEmpty()) {
            binding.titleTextBox.error = getString(R.string.empty_title)
            binding.titleTextBox.setBackgroundResource(R.drawable.border_warning) // Terapkan border merah
            isValid = false
        } else if (wordCount > maxWords) {
            binding.titleTextBox.error = getString(R.string.max_words)
            binding.titleTextBox.setBackgroundResource(R.drawable.border_warning) // Terapkan border merah
            isValid = false
        } else {
            binding.titleTextBox.error = null
            binding.titleTextBox.setBackgroundResource(R.drawable.border_default) // Terapkan border default jika valid
        }

        // Validasi untuk description (jika kosong)
        if (description.isEmpty()) {
            binding.descTextBox.error = getString(R.string.empty_desc)
            binding.descTextBox.setBackgroundResource(R.drawable.border_warning) // Terapkan border merah
            isValid = false
        } else {
            binding.descTextBox.error = null
            binding.descTextBox.setBackgroundResource(R.drawable.border_default) // Terapkan border default jika valid
        }

        return isValid
    }

    private fun setupWordCount() {
        val maxWords = 1000
        binding.descTextBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val wordCount = s?.trim()?.split("\\s+".toRegex())?.filter { it.isNotEmpty() }?.size ?: 0

                // Update the word count display
                binding.wordCountDisplay.text = "$wordCount/$maxWords words"

                // Tambahkan visual feedback jika word count mendekati batas
                if (wordCount > maxWords) {
                    binding.wordCountDisplay.setTextColor(ContextCompat.getColor(this@UploadActivity, R.color.red))
                    binding.descTextBox.setBackgroundResource(R.drawable.border_warning)
                } else {
                    binding.wordCountDisplay.setTextColor(ContextCompat.getColor(this@UploadActivity, R.color.black))
                    binding.descTextBox.setBackgroundResource(R.drawable.border_default)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE

    }
}