package com.dicoding.escore.view.upload

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.escore.R
import com.dicoding.escore.data.remote.Result
import com.dicoding.escore.databinding.ActivitySignUpBinding
import com.dicoding.escore.databinding.ActivityUploadBinding
import com.dicoding.escore.view.ViewModelFactory
import com.dicoding.escore.view.ViewModelFactoryML
import com.dicoding.escore.view.login.LoginActivity
import com.dicoding.escore.view.resultUpload.ResultUploadActivity
import com.dicoding.escore.view.signup.SignUpActivity
import com.dicoding.escore.view.signup.SignUpViewModel

//class UploadActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityUploadBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityUploadBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        binding.submitButton.setOnClickListener {
////            Ubah disini
//
//        }
//    }
//}

//class UploadActivity : AppCompatActivity(), ClassifierListener {
//
//    private lateinit var binding: ActivityUploadBinding
//    private lateinit var textClassifierHelper: TextClassifierHelper
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityUploadBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        // Inisialisasi TextClassifierHelper
//        textClassifierHelper = TextClassifierHelper(
//            context = this,
//            classifierListener = this // Set listener ke activity ini
//        )
//
//        binding.submitButton.setOnClickListener {
//            val inputText = binding.largeTextBox.text.toString()
//
//            if (inputText.isNotBlank()) {
//                textClassifierHelper.classifyText(inputText) // Lakukan klasifikasi
//            } else {
//                Toast.makeText(this, "Masukkan teks terlebih dahulu!", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    // Implementasi callback hasil
//    override fun onResult(result: String) {
//        runOnUiThread {
//            // Pindah ke ResultUploadActivity dengan hasil prediksi
//            val intent = Intent(this, ResultUploadActivity::class.java)
//            intent.putExtra("PREDICT_RESULT", result) // Kirim hasil prediksi
//            startActivity(intent)
//        }
//    }
//
//    override fun onError(error: String) {
//        runOnUiThread {
//            Toast.makeText(this, "Terjadi kesalahan: $error", Toast.LENGTH_LONG).show()
//        }
//    }
//}

class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding

    private val viewModel by viewModels<UploadViewModel> {
        ViewModelFactoryML.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.submitButton.setOnClickListener {
            val text = binding.largeTextBox.text.toString().trim()
            viewModel.predict(text)
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }
        viewModel.predictResult.observe(this) { result ->
            when(result) {
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Success -> {
                    showLoading(false)
                    val score = result.data.predictedResult?.score ?: 0 // Nilai default jika null
                    val suggestion = result.data.predictedResult?.suggestion ?: "Tidak ada saran" // Nilai default jika null
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