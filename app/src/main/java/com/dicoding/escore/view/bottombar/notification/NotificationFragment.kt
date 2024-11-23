package com.dicoding.escore.view.bottombar.notification

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.dicoding.escore.R
import com.dicoding.escore.databinding.FragmentHomeBinding
import com.dicoding.escore.databinding.FragmentNotificationBinding
import com.dicoding.escore.view.bottombar.home.HomeViewModel

class NotificationFragment : Fragment() {

    private var _binding: FragmentNotificationBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(NotificationViewModel::class.java)

        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotification
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}