package com.submission.storyapp.helper

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.submission.storyapp.di.Injection
import com.submission.storyapp.repository.UserRepository
import com.submission.storyapp.view.addstory.AddStoryViewModel
import com.submission.storyapp.view.login.LoginViewModel
import com.submission.storyapp.view.main.MainViewModel
import com.submission.storyapp.view.signup.SignUpViewModel

class ViewModelFactory(private val userRepository: UserRepository): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(userRepository) as T
            }
            else -> throw  IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }



    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}