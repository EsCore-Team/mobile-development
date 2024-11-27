package com.dicoding.escore.view.upload

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.escore.R
import com.dicoding.escore.databinding.ActivitySignUpBinding
import com.dicoding.escore.databinding.ActivityUploadBinding
import com.dicoding.escore.utils.ClassifierListener
import com.dicoding.escore.utils.TextClassifierHelper
import com.dicoding.escore.view.login.LoginActivity
import com.dicoding.escore.view.resultUpload.ResultUploadActivity
import com.dicoding.escore.view.signup.SignUpActivity

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

class UploadActivity : AppCompatActivity(), ClassifierListener {

    private lateinit var binding: ActivityUploadBinding
    private lateinit var textClassifierHelper: TextClassifierHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi TextClassifierHelper
        textClassifierHelper = TextClassifierHelper(
            context = this,
            classifierListener = this // Set listener ke activity ini
        )

        binding.submitButton.setOnClickListener {
            val inputText = binding.largeTextBox.text.toString()

            if (inputText.isNotBlank()) {
                textClassifierHelper.classifyText(inputText) // Lakukan klasifikasi
            } else {
                Toast.makeText(this, "Masukkan teks terlebih dahulu!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Implementasi callback hasil
    override fun onResult(result: String) {
        runOnUiThread {
            // Pindah ke ResultUploadActivity dengan hasil prediksi
            val intent = Intent(this, ResultUploadActivity::class.java)
            intent.putExtra("PREDICT_RESULT", result) // Kirim hasil prediksi
            startActivity(intent)
        }
    }

    override fun onError(error: String) {
        runOnUiThread {
            Toast.makeText(this, "Terjadi kesalahan: $error", Toast.LENGTH_LONG).show()
        }
    }
}
