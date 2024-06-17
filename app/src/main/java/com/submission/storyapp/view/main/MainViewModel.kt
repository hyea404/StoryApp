package com.submission.storyapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.submission.storyapp.data.preferences.UserModel
import com.submission.storyapp.data.response.ListStoryItem
import com.submission.storyapp.repository.UserRepository
import com.submission.storyapp.helper.Result
import kotlinx.coroutines.launch

class MainViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _listStoryItem = MediatorLiveData<Result<List<ListStoryItem>>>()
    val listStoryItem: LiveData<Result<List<ListStoryItem>>> = _listStoryItem

    fun getStories(token: String) {
        val liveData = userRepository.getStories(token)
        _listStoryItem.addSource(liveData) { result ->
            _listStoryItem.value = result
        }
    }

    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }

    fun logOut() {
        viewModelScope.launch {
            userRepository.logout()
        }
    }

    fun listStoryLocation(token: String): LiveData<PagingData<ListStoryItem>> =
        userRepository.getStoriesLocation(token).cachedIn(viewModelScope)


}