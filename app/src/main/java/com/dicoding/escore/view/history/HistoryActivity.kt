package com.dicoding.escore.view.history

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.dicoding.escore.R
import com.dicoding.escore.data.remote.UserRepository
import com.dicoding.escore.data.remote.response.HistoryResponse
import com.dicoding.escore.data.remote.retrofit.ApiService
import com.dicoding.escore.databinding.ActivityHistoryBinding
import com.dicoding.escore.databinding.ActivityResultUploadBinding
import com.dicoding.escore.pref.SessionManager
import com.dicoding.escore.view.ViewModelFactory
import com.dicoding.escore.view.login.LoginViewModel
import com.dicoding.escore.view.signup.SignUpActivity

class HistoryActivity : AppCompatActivity() {
    private val viewModel by viewModels<HistoryViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        // Initialize dependencies
        sessionManager = SessionManager(this)

        // Observe LiveData
        viewModel.historyLiveData.observe(this) { response ->
            handleResponse(response)
        }

        viewModel.errorLiveData.observe(this) { error ->
            showToast(error)
        }

        // Fetch history
        viewModel.fetchHistory(
            createdAt = "2024-11-30",
            title = "Sample Title",
            essay = "Sample Essay",
            score = "85"
        )
    }

    private fun handleResponse(response: HistoryResponse) {
        // Handle and display response
        // Example: Log predictions
        response.predictions?.forEach { prediction ->
            Log.d("HistoryActivity", "Prediction: $prediction")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
