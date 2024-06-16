package com.submission.storyapp.view.signup

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import com.submission.storyapp.data.response.ResponseUserRegister
import com.submission.storyapp.repository.UserRepository
import com.submission.storyapp.helper.Result


class SignUpViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _register = MediatorLiveData<Result<ResponseUserRegister>>()
    val registerViewModel: LiveData<Result<ResponseUserRegister>> = _register

    fun register(name: String, email: String, password: String) {
        val liveData = userRepository.register(name, email, password)
        _register.addSource(liveData) { result ->
            _register.value = result
        }
    }

}