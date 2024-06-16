package com.submission.storyapp.view.signup

import android.animation.ObjectAnimator
import android.animation.AnimatorSet
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Build
import androidx.activity.viewModels
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import android.view.WindowInsets
import androidx.appcompat.app.AlertDialog
import com.submission.storyapp.R
import com.submission.storyapp.databinding.ActivitySignupBinding
import com.submission.storyapp.helper.ViewModelFactory
import com.submission.storyapp.helper.Result
import com.submission.storyapp.view.login.LoginActivity

class SignupActivity : AppCompatActivity() {
    private val viewModel by viewModels<SignUpViewModel> {
        ViewModelFactory.getInstance(this)
    }

    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setRegister()
        setView()
        setAction()
        playAnimate()
    }

    private fun setRegister() {
        viewModel.registerViewModel.observe(this) {
            when (it) {
                is Result.Loading -> {
                    showLoading(
                        true
                    )
                }

                is Result.Success -> {
                    showLoading(false)
                    AlertDialog.Builder(this).apply {
                        setTitle("Congrats!")
                        setMessage("Your account has been created")
                        setCancelable(false)
                        setPositiveButton("Login") { _, _ ->
                            val intent = Intent(context, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        create()
                        show()
                    }
                }

                is Result.Error -> {
                    Log.e("RegisterActivity", "error: ${it.error}")
                    showToast()
                    showLoading(false)
                }
            }
        }
    }

    private fun setAction() {
        binding.signupBtn.setOnClickListener {
            binding.apply {
                if (validInput()) {
                    val name = nameEditText.text.toString().trim()
                    val email = emailEditText.text.toString().trim()
                    val password = passwordEditText.text.toString().trim()
                    viewModel.register(name, email, password)
                } else {
                    showToast()
                }
            }
        }
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

    private fun validInput(): Boolean {
        return binding.nameTv.error.isNullOrBlank() &&
                binding.emailTv.error.isNullOrBlank() &&
                binding.passwordTv.error.isNullOrBlank()
    }

    private fun playAnimate() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 5500
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val titleTextView = ObjectAnimator.ofFloat(binding.titleTv, View.ALPHA, 1f).setDuration(110)
        val nameTextView = ObjectAnimator.ofFloat(binding.nameTv, View.ALPHA, 1f).setDuration(110)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.nameEtl, View.ALPHA, 1f).setDuration(110)
        val emailTextView = ObjectAnimator.ofFloat(binding.emailTv, View.ALPHA, 1f).setDuration(110)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEtl, View.ALPHA, 1f).setDuration(110)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTv, View.ALPHA, 1f).setDuration(110)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEtl, View.ALPHA, 1f).setDuration(110)
        val signupButton =
            ObjectAnimator.ofFloat(binding.signupBtn, View.ALPHA, 1f).setDuration(110)

        AnimatorSet().apply {
            playSequentially(
                titleTextView, nameTextView, nameEditTextLayout, emailTextView,
                emailEditTextLayout, passwordTextView, passwordEditTextLayout, signupButton
            )
            start()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast() {
        Toast.makeText(this, R.string.registration_fail, Toast.LENGTH_SHORT).show()
    }
}