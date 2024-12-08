package com.dicoding.escore.view.bottombar.home

import android.content.Intent
import android.os.Build
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.escore.R
import com.dicoding.escore.adapter.HistoryAdapter
import com.dicoding.escore.data.remote.Result
import com.dicoding.escore.databinding.FragmentHomeBinding
import com.dicoding.escore.view.ViewModelFactory
import com.dicoding.escore.view.detailHistory.DetailHistoryActivity
import com.dicoding.escore.view.history.HistoryActivity
import com.dicoding.escore.view.upload.UploadActivity

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var adapter: HistoryAdapter

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        requireActivity().window.apply {
            // Ubah warna status bar
            statusBarColor = ContextCompat.getColor(requireContext(), R.color.colorPrimary)

            decorView.systemUiVisibility = decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }


        // Inisialisasi RecyclerView
        setupRecyclerView()

        // Observasi data history
        observeViewModel()

        viewModel.fetchHistory(createdAt = "", title = "", score = "")

        binding.submitButton.setOnClickListener {
            val intent = Intent(requireContext(), UploadActivity::class.java)
            startActivity(intent)
        }

        viewModel.fullName.observe(viewLifecycleOwner) { fullName ->
            binding.textName.text = fullName
        }

        binding.tvViewAll.setOnClickListener {
            val intent = Intent(requireContext(), HistoryActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = HistoryAdapter { id ->
            val intent = Intent(requireContext(), DetailHistoryActivity::class.java)
            intent.putExtra("EXTRA_ID", id)
            startActivity(intent)
        }
        binding.rvHistory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@HomeFragment.adapter
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE

    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.historyLiveData.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> showLoading(true)
                is Result.Success -> {
                    showLoading(false)
                    val predictions = result.data.predictions?.filterNotNull()?.sortedByDescending {
                        it.createdAt
                    }

                    // Batasi hanya 2 item pertama
                    val limitedPredictions = predictions?.take(2)

                    limitedPredictions?.let { sortedList ->
                        adapter.setItems(sortedList)
                        binding.rvHistory.visibility = if (sortedList.isNotEmpty()) View.VISIBLE else View.GONE
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
                            Toast.makeText(requireContext(), "Error connection", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        viewModel.noDataVisible.observe(viewLifecycleOwner) { isVisible ->
            binding.tvNoData.visibility = if (isVisible) View.VISIBLE else View.GONE
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
