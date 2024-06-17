package com.submission.storyapp.view.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.submission.storyapp.data.preferences.UserModel
import com.submission.storyapp.data.response.ListStoryItem
import com.submission.storyapp.repository.UserRepository
import com.submission.storyapp.helper.Result

class MapsViewModel(private val userRepository: UserRepository): ViewModel() {
    private val _mapsResponse = MediatorLiveData<Result<List<ListStoryItem>>>()
    val mapsResponse: LiveData<Result<List<ListStoryItem>>> = _mapsResponse

    fun getLocationUser(token: String) {
        val liveData= userRepository.getStoriesWithLocation((token))
        _mapsResponse.addSource(liveData) {
                result -> _mapsResponse.value = result
        }
    }

    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }
}