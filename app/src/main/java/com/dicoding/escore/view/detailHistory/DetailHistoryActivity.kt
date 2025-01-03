package com.dicoding.escore.view.detailHistory

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.dicoding.escore.R
import com.dicoding.escore.data.remote.Result
import com.dicoding.escore.databinding.ActivityDetailHistoryBinding
import com.dicoding.escore.view.ViewModelFactory


class DetailHistoryActivity : AppCompatActivity() {

    private val viewModel by viewModels<DetailHistoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivityDetailHistoryBinding
    private var isDescriptionExpanded = false

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

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar) // Set Toolbar as the ActionBar
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbar.navigationIcon?.setTint(getColor(R.color.black))


        binding.toolbar.setNavigationOnClickListener {
            onBackPressed() // Aksi tombol back
        }

        observeViewModel()

        // Fetch detail
        viewModel.fetchDetailHistory(email, id)

        binding.btnMoreContainer.setOnClickListener {
            if (isDescriptionExpanded) {
                // Collapse deskripsi
                binding.description.maxLines = 5
                binding.description.ellipsize = TextUtils.TruncateAt.END
                binding.btnMore.text = getString(R.string.more)
                binding.iconMore.setImageResource(R.drawable.baseline_expand_more_24)
            } else {
                // Expand deskripsi
                binding.description.maxLines = Int.MAX_VALUE
                binding.description.ellipsize = null
                binding.btnMore.text = getString(R.string.less)
                binding.iconMore.setImageResource(R.drawable.baseline_expand_less_24)
            }
            isDescriptionExpanded = !isDescriptionExpanded
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE

    }

//    private fun observeViewModel() {
//        viewModel.detailLiveData.observe(this) { detail ->
//            binding.textResultUpload.text = detail?.predictedResult?.score
//            binding.suggestion.text = detail?.predictedResult?.suggestion
//            binding.tvEssayTitle.text = detail?.title
//            binding.description.text = detail?.essay
//        }
//
//        viewModel.errorLiveData.observe(this) { error ->
//            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
//        }
//    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.detailLiveData.observe(this) { detail ->
            when (detail) {
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    showLoading(false)
                    binding.textResultUpload.text = detail.data?.predictedResult?.score
                    binding.suggestion.text = detail.data?.predictedResult?.suggestion
                    binding.tvEssayTitle.text = detail.data?.title
                    binding.description.text = detail.data?.essay
                }
                is Result.Error -> {
                    showLoading(false)
                    val errorMessage = detail.error ?: "Unknown error"
                    if (errorMessage.contains("Connection error", true) || errorMessage.contains("unable to resolve host", true)) {
                        // Tampilkan toast jika ada masalah koneksi atau masalah host
                        Toast.makeText(this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}