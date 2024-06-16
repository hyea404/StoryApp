package com.submission.storyapp.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MediatorLiveData
import com.submission.storyapp.data.response.ResponseUserLogin
import com.submission.storyapp.repository.UserRepository
import com.submission.storyapp.helper.Result

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _loginAction = MediatorLiveData<Result<ResponseUserLogin>>()
    val loginActionModel: LiveData<Result<ResponseUserLogin>> = _loginAction

    fun login(email: String, password: String) {
        val liveData = userRepository.login(email, password)
        _loginAction.addSource(liveData) { result ->
            _loginAction.value = result
        }
    }
}