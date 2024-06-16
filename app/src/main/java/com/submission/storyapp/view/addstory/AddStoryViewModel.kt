package com.submission.storyapp.view.addstory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.MediatorLiveData
import com.submission.storyapp.data.preferences.UserModel
import com.submission.storyapp.data.response.ResponseNewAddStory
import com.submission.storyapp.helper.Result
import com.submission.storyapp.repository.UserRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _insertStoryResponse = MediatorLiveData<Result<ResponseNewAddStory>>()
    val insertStoryResponse: LiveData<Result<ResponseNewAddStory>> = _insertStoryResponse

    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }

    fun addStory(token: String, file: MultipartBody.Part, description: RequestBody) {
        val liveData = userRepository.addStoryApp(token, file, description)
        _insertStoryResponse.addSource(liveData) { result ->
            _insertStoryResponse.value = result
        }
    }


}