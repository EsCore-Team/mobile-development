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

    private val historyViewModel: HistoryViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    private lateinit var historyAdapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Inisialisasi RecyclerView
        setupRecyclerView()

        // Observasi data history
        observeHistory()

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
        historyAdapter = HistoryAdapter { id ->
            val intent = Intent(requireContext(), DetailHistoryActivity::class.java)
            intent.putExtra("EXTRA_ID", id)
            startActivity(intent)
        }
        binding.rvHistory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = historyAdapter
        }
    }

    private fun observeHistory() {
        historyViewModel.historyLiveData.observe(viewLifecycleOwner) { response ->
            response?.predictions?.let { predictions ->
                // Membalik urutan data berdasarkan createdAt
                val sortedList = predictions.filterNotNull()
                    .sortedByDescending { it.createdAt }
                    .take(3) // Membatasi hanya 3 item

                // Mengatur data ke adapter
                historyAdapter.setItems(sortedList)
            }
        }

        historyViewModel.errorLiveData.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }

        // Fetch data
        historyViewModel.fetchHistory(createdAt = "", title = "", score = "")
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
