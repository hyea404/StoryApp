package com.submission.storyapp.view.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.submission.storyapp.R
import android.animation.ObjectAnimator
import android.animation.AnimatorSet
import android.content.Intent
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.os.Build
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.submission.storyapp.databinding.ActivityLoginBinding
import com.submission.storyapp.helper.Result
import com.submission.storyapp.helper.ViewModelFactory
import com.submission.storyapp.view.main.MainActivity


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setLoginView()
        setLoginAction()
        playAnimationLogin()
        viewModel.loginActionModel.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    showLoading(true)
                }

                is Result.Success -> {
                    showLoading(false)
                    AlertDialog.Builder(this).apply {
                        setTitle("Login Succeess!")
                        setMessage(R.string.message_login_page)
                        setPositiveButton("Let's Start!") { _, _ ->
                            val intent = Intent(context, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            finish()
                        }
                        create()
                        show()
                    }
                }

                is Result.Error -> {
                    toastFailed()
                    showLoading(false)
                }
            }
        }

    }

    private fun setLoginAction() {
        binding.loginButton.setOnClickListener {
            binding.apply {
                val email = emailEditText.text.toString().trim()
                val password = passwordEditText.text.toString().trim()

                if (email.isNotEmpty() && password.isNotEmpty()) {
                    viewModel.login(email, password)
                } else {
                    Toast.makeText(
                        this@LoginActivity,
                        "Email and password must be filled in!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun setLoginView() {
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

    private fun playAnimationLogin() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 5500
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val titleAnimator = ObjectAnimator.ofFloat(binding.titleTv, View.ALPHA, 1f).setDuration(250)
        val messageAnimator =
            ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(250)
        val emailTextViewAnimator =
            ObjectAnimator.ofFloat(binding.emailTv, View.ALPHA, 1f).setDuration(250)
        val emailEditTextLayoutAnimator =
            ObjectAnimator.ofFloat(binding.emailEtl, View.ALPHA, 1f).setDuration(250)
        val passwordTextViewAnimator =
            ObjectAnimator.ofFloat(binding.passwordTv, View.ALPHA, 1f).setDuration(250)
        val passwordEditTextLayoutAnimator =
            ObjectAnimator.ofFloat(binding.passwordEtl, View.ALPHA, 1f).setDuration(250)
        val loginButtonAnimator =
            ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(550)

        AnimatorSet().apply {
            playSequentially(
                titleAnimator,
                messageAnimator,
                emailTextViewAnimator,
                emailEditTextLayoutAnimator,
                passwordTextViewAnimator,
                passwordEditTextLayoutAnimator,
                loginButtonAnimator
            )
            startDelay = 110
        }.start()
    }

    private fun toastFailed() {
        Toast.makeText(
            this,
            R.string.failed_login,
            Toast.LENGTH_SHORT
        ).show()
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}