package com.dicoding.escore.view.bottombar.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.dicoding.escore.R
import com.dicoding.escore.databinding.FragmentProfileBinding
import com.dicoding.escore.pref.SessionManager
import com.dicoding.escore.view.ViewModelFactory
import com.dicoding.escore.view.onboarding.OnboardingActivity

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Inisialisasi ViewModel menggunakan ViewModelFactory
        val factory = ViewModelFactory.getInstance(requireContext())
        profileViewModel = ViewModelProvider(this, factory).get(ProfileViewModel::class.java)

        requireActivity().window.apply {
            statusBarColor = ContextCompat.getColor(requireContext(), R.color.white)

            // Gunakan fallback untuk versi di bawah Android R
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        // Observe LiveData untuk fullName
        profileViewModel.fullName.observe(viewLifecycleOwner) { fullName ->
            binding.userName.text = fullName
        }

        // Observe LiveData untuk email
        profileViewModel.email.observe(viewLifecycleOwner) { email ->
            binding.userEmail.text = email
        }


        setupAction()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupAction() {
        binding.logoutButton.setOnClickListener {
            showLogoutConfirmation()
        }
    }

    private fun showLogoutConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.confirm_logout))
            .setMessage(getString(R.string.are_you_sure_you_want_to_logout))
            .setPositiveButton(getString(R.string.yes)) { dialog, _ ->
                logout()
                dialog.dismiss()
            }
            .setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun logout() {
        val sessionManager = SessionManager(requireContext())
        sessionManager.clearAuthToken()
        navigateToLogin()
    }

    private fun navigateToLogin() {
        val intent = Intent(requireContext(), OnboardingActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}
