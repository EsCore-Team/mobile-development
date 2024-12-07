package com.dicoding.escore.view.bottombar.guide

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.dicoding.escore.R
import com.dicoding.escore.databinding.FragmentGuideBinding

class GuideFragment : Fragment() {

    private var _binding: FragmentGuideBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(GuideViewModel::class.java)

        requireActivity().window.apply {
            statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)

            // Gunakan fallback untuk versi di bawah Android R
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        _binding = FragmentGuideBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}