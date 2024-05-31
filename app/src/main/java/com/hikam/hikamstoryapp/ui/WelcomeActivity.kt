package com.hikam.hikamstoryapp.ui

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.hikam.hikamstoryapp.ui.RegisterActivity
import com.hikam.hikamstoryapp.databinding.ActivityWelcomeBinding


class WelcomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWelcomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        playAnimation()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            startActivity(Intent(this, UserLoginActivity::class.java))
        }

        binding.signupButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun playAnimation() {
        val rotateAnimator = ObjectAnimator.ofFloat(binding.imageView, View.ROTATION, 0f, 360f).apply {
            duration = 2000
        }

        val translateAnimator = ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 1000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }

        val fadeAnimators = mutableListOf<ObjectAnimator>()
        val viewsToAnimate = listOf(
            binding.loginButton,
            binding.signupButton,
            binding.titleTextView,
            binding.descTextView,
        )

        viewsToAnimate.forEachIndexed { index, view ->
            fadeAnimators.add(ObjectAnimator.ofFloat(view, View.ALPHA, 0f, 1f).apply {
                duration = 500 // Durasi animasi
                startDelay = (index * 100).toLong() // Delay untuk setiap elemen
            })
        }

        val rotateLoginAnimator = ObjectAnimator.ofFloat(binding.loginButton, View.ROTATION, 0f, 360f).apply {
            duration = 1000
        }
        val rotateRegisterAnimator = ObjectAnimator.ofFloat(binding.signupButton, View.ROTATION, 0f, 360f).apply {
            duration = 1000
        }

        AnimatorSet().apply {
            playSequentially(rotateAnimator)
            playTogether(fadeAnimators as Collection<Animator>?)
            play(translateAnimator)
            play(rotateLoginAnimator)
            play(rotateRegisterAnimator)
            startDelay = 100
            start()
        }
    }
}