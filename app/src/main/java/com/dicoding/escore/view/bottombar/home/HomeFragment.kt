package com.dicoding.escore.view.bottombar.home

import android.content.Intent
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.escore.R
import com.dicoding.escore.adapter.HistoryAdapter
import com.dicoding.escore.data.remote.Result
import com.dicoding.escore.databinding.FragmentHomeBinding
import com.dicoding.escore.view.ViewModelFactory
import com.dicoding.escore.view.detailHistory.DetailHistoryActivity
import com.dicoding.escore.view.history.HistoryActivity
import com.dicoding.escore.view.history.HistoryViewModel
import com.dicoding.escore.view.login.LoginActivity
import com.dicoding.escore.view.onboarding.OnboardingActivity
import com.dicoding.escore.view.signup.SignUpActivity
import com.dicoding.escore.view.upload.UploadActivity

//class HomeFragment : Fragment() {
//
//    private var _binding: FragmentHomeBinding? = null
//
//    // This property is only valid between onCreateView and
//    // onDestroyView.
//    private val binding get() = _binding!!
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        val homeViewModel =
//            ViewModelProvider(this).get(HomeViewModel::class.java)
//
//        _binding = FragmentHomeBinding.inflate(inflater, container, false)
//        val root: View = binding.root
//
//        binding.submitButton.setOnClickListener {
//            val intent = Intent(requireContext(), UploadActivity::class.java)
//            startActivity(intent)
//            requireActivity()
//        }
//
//        binding.tvViewAll.setOnClickListener {
//            val intent = Intent(requireContext(), HistoryActivity::class.java)
//            startActivity(intent)
//            requireActivity()
//        }
//
//
//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
//        return root
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var adapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Inisialisasi RecyclerView
        setupRecyclerView()

        // Observasi data history
        observeViewModel()

        viewModel.fetchHistory(createdAt = "", title = "", score = "")

        binding.submitButton.setOnClickListener {
            val intent = Intent(requireContext(), UploadActivity::class.java)
            startActivity(intent)
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
                is Result.Loading -> {
                    showLoading(true)
                }
                is Result.Success -> {
                    showLoading(false)
                    val predictions = result.data.predictions
                        ?.filterNotNull()
                        ?.sortedByDescending { it.createdAt }
                        ?.take(2)
                    predictions?.let { sortedList ->
                        adapter.setItems(sortedList)
                    }
                }
                is Result.Error -> {
                    showLoading(false)
                    Toast.makeText(requireContext(), result.error, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
