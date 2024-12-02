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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.escore.R
import com.dicoding.escore.adapter.HistoryAdapter
import com.dicoding.escore.data.remote.UserRepository
import com.dicoding.escore.data.remote.response.HistoryResponse
import com.dicoding.escore.data.remote.retrofit.ApiService
import com.dicoding.escore.databinding.ActivityHistoryBinding
import com.dicoding.escore.databinding.ActivityResultUploadBinding
import com.dicoding.escore.pref.SessionManager
import com.dicoding.escore.view.ViewModelFactory
import com.dicoding.escore.view.detailHistory.DetailHistoryActivity
import com.dicoding.escore.view.login.LoginViewModel
import com.dicoding.escore.view.signup.SignUpActivity

//class HistoryActivity : AppCompatActivity() {
//    private val viewModel by viewModels<HistoryViewModel> {
//        ViewModelFactory.getInstance(this)
//    }
//    private lateinit var adapter: HistoryAdapter
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_history)
//
//        adapter = HistoryAdapter()
//        val recyclerView: RecyclerView = findViewById(R.id.rv_history)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.adapter = adapter
//
//        observeViewModel()
//
//        // Fetch history
//        viewModel.fetchHistory(createdAt = "", title = "", score = "")
//    }
//
//    private fun observeViewModel() {
//        viewModel.historyLiveData.observe(this) { response ->
//            response?.predictions?.let { predictions ->
//                adapter.setItems(predictions.filterNotNull())
//            }
//        }
//
//        viewModel.errorLiveData.observe(this) { error ->
//            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
//        }
//    }
//}

class HistoryActivity : AppCompatActivity() {
    private val viewModel by viewModels<HistoryViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var adapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        adapter = HistoryAdapter { id ->
            val intent = Intent(this, DetailHistoryActivity::class.java)
            intent.putExtra("EXTRA_ID", id)
            startActivity(intent)
        }

        val recyclerView: RecyclerView = findViewById(R.id.rv_history)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        observeViewModel()

        // Fetch history
        viewModel.fetchHistory(createdAt = "", title = "", score = "")
    }

    private fun observeViewModel() {
        viewModel.historyLiveData.observe(this) { response ->
            response?.predictions?.let { predictions ->
                // Membalikkan urutan data berdasarkan createdAt
                val sortedList = predictions.filterNotNull().sortedByDescending {
                    it.createdAt
                }
                adapter.setItems(sortedList)
            }
        }

        viewModel.errorLiveData.observe(this) { error ->
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
    }

}
