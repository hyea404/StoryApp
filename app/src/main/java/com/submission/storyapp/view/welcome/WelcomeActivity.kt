package com.submission.storyapp.view.welcome

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.WindowInsets
import android.view.WindowManager
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.submission.storyapp.databinding.ActivityWelcomeBinding
import com.submission.storyapp.view.login.LoginActivity
import com.submission.storyapp.view.signup.SignupActivity

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setView()

        binding.loginButton.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.signupBtn.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        playAnimate()

    }

    private fun setView() {
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


    private fun playAnimate() {
        val imgRotate =
            ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_Y, 0f, 200f).apply {
                duration = 2000
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
                interpolator = DecelerateInterpolator()

            }

        val yTranslationLogin =
            ObjectAnimator.ofFloat(binding.loginButton, View.TRANSLATION_Y, 50f, 0f)
                .setDuration(510)
        val alphaLogin =
            ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 0f, 1f).setDuration(510)

        val yTranslationSignup =
            ObjectAnimator.ofFloat(binding.signupBtn, View.TRANSLATION_Y, 50f, 0f).setDuration(510)
        val alphaSignup =
            ObjectAnimator.ofFloat(binding.signupBtn, View.ALPHA, 0f, 1f).setDuration(510)

        val ytranslationTitle =
            ObjectAnimator.ofFloat(binding.titleTv, View.TRANSLATION_Y, -30f, 0f).setDuration(510)
        val alphaTitle =
            ObjectAnimator.ofFloat(binding.titleTv, View.ALPHA, 0f, 1f).setDuration(510)

        val yTranslationDesc =
            ObjectAnimator.ofFloat(binding.descTextView, View.TRANSLATION_Y, -30f, 0f)
                .setDuration(510)
        val alphaDesc =
            ObjectAnimator.ofFloat(binding.descTextView, View.ALPHA, 0f, 1f).setDuration(510)

        val set = AnimatorSet()
        set.playTogether(
            imgRotate,
            yTranslationLogin, alphaLogin,
            yTranslationSignup, alphaSignup,
            ytranslationTitle, alphaTitle,
            yTranslationDesc, alphaDesc
        )
        set.start()
    }
}