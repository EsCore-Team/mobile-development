package com.dicoding.escore.view.detailHistory

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.dicoding.escore.R
import com.dicoding.escore.databinding.ActivityDetailHistoryBinding
import com.dicoding.escore.view.ViewModelFactory


class DetailHistoryActivity : AppCompatActivity() {

    private val viewModel by viewModels<DetailHistoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityDetailHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize ViewBinding
        binding = ActivityDetailHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra("EXTRA_ID")
        if (id.isNullOrEmpty()) {
            Toast.makeText(this, "ID not found.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val email = viewModel.getUserEmail()
        if (email.isNullOrEmpty()) {
            Toast.makeText(this, "Email not found.", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        observeViewModel()

        // Fetch detail
        viewModel.fetchDetailHistory(email, id)
    }

    private fun observeViewModel() {
        viewModel.detailLiveData.observe(this) { detail ->
            binding.textResultUpload.text = detail?.predictedResult?.score
            binding.suggestion.text = detail?.predictedResult?.suggestion
            binding.tvEssayTitle.text = detail?.title
            binding.description.text = detail?.essay
        }

        viewModel.errorLiveData.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
    }
}