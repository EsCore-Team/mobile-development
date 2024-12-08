package com.dicoding.escore.view.onboarding

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.escore.databinding.ActivityOnboardingBinding
import com.dicoding.escore.view.login.LoginActivity

class OnboardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        playAnimation()
    }

    private fun setupAction() {
        binding.startButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

    }

    private fun playAnimation() {

        val next = ObjectAnimator.ofFloat(binding.startButton, View.ALPHA, 1f).setDuration(200)
        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(200)
        val desc = ObjectAnimator.ofFloat(binding.descTextView, View.ALPHA, 1f).setDuration(200)

        AnimatorSet().apply {
            playSequentially(title, desc, next)
            start()
        }
    }
}