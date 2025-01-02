package com.dicoding.escore.view.history

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.escore.R
import com.dicoding.escore.adapter.HistoryAdapter
import com.dicoding.escore.data.local.entity.HistoryEntity
import com.dicoding.escore.databinding.ActivityHistoryBinding
import com.dicoding.escore.view.ViewModelFactory
import com.dicoding.escore.view.detailHistory.DetailHistoryActivity
import com.dicoding.escore.data.remote.Result
import com.dicoding.escore.data.remote.response.PredictedResult2
import com.dicoding.escore.data.remote.response.PredictionsItem
import com.dicoding.escore.view.bottombar.home.HomeViewModel


class HistoryActivity : AppCompatActivity() {
    private val viewModel by viewModels<HistoryViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var adapter: HistoryAdapter
    private lateinit var binding: ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = HistoryAdapter { id ->
            val intent = Intent(this, DetailHistoryActivity::class.java)
            intent.putExtra("EXTRA_ID", id)
            startActivity(intent)
        }

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar) // Set Toolbar as the ActionBar
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.toolbar.navigationIcon?.setTint(getColor(R.color.black))


        binding.toolbar.setNavigationOnClickListener {
            onBackPressed() // Aksi tombol back
        }

        val recyclerView: RecyclerView = findViewById(R.id.rv_history)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        observeViewModel()

        // Fetch history
        viewModel.fetchHistory(createdAt = "", title = "", score = "")

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE

    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.historyLiveData.observe(this) { result ->
            when (result) {
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    showLoading(false)
                    val sortedList = result.data.sortedByDescending { it.createdAt }
                    if (sortedList.isNotEmpty()) {
                        adapter.setItems(sortedList)
                        binding.rvHistory.visibility = View.VISIBLE
                        binding.tvNoData.visibility = View.GONE
                    } else {
                        binding.rvHistory.visibility = View.GONE
                        binding.tvNoData.visibility = View.VISIBLE
                    }
                }
                is Result.Error -> {
                    showLoading(false)
                    when (result.error) {
                        "No Data" -> {
                            binding.rvHistory.visibility = View.GONE
                            binding.tvNoData.visibility = View.VISIBLE
                        }
                        "Error connection" -> {
                            Toast.makeText(this, getString(R.string.connection_error), Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}
