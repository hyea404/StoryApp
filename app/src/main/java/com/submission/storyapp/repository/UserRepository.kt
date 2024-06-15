package com.submission.storyapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.submission.storyapp.data.preferences.UserModel
import com.submission.storyapp.data.preferences.UserModelPreferences
import com.submission.storyapp.data.response.ListStoryItem
import com.submission.storyapp.data.response.ResponseNewAddStory
import com.submission.storyapp.data.response.ResponseUserLogin
import com.submission.storyapp.data.response.ResponseUserRegister
import com.submission.storyapp.data.retrofit.ApiService
import com.submission.storyapp.helper.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UserRepository private constructor( private val modelUserPreferences: UserModelPreferences, private val apiServices: ApiService
){
    fun register (name: String, email: String, password: String):LiveData<Result<ResponseUserRegister>> = liveData(Dispatchers.IO)
    {
        emit(Result.Loading)
        try {
            val response = apiServices.register(name, email, password)
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    suspend fun saveSessionLogin(user: UserModel) {
        modelUserPreferences.saveSessionLogin(user)
    }


    fun login(email: String, password: String): LiveData<Result<ResponseUserLogin>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = apiServices.login(email, password)
            val token = response.loginResult.token
            saveSessionLogin(UserModel(email, token))
            emit(Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getSession() : Flow<UserModel> {
        return modelUserPreferences.getSession()
    }
    fun getStories(token: String): LiveData<Result<List<ListStoryItem>>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = apiServices.getStories(("Bearer $token"))
            val story = response.listStory
            emit(Result.Success(story))

        } catch (e: Exception){
            emit(Result.Error(e.message.toString()))
        }
    }




    fun addStoryApp(token: String, file:MultipartBody.Part ,description: RequestBody): LiveData<Result<ResponseNewAddStory>> = liveData(Dispatchers.IO) {
        emit(Result.Loading)
        try {
            val response = apiServices.uploadStories("Bearer $token", file, description)
            emit (Result.Success(response))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }








    suspend fun logout() {
        modelUserPreferences.logout()
    }

    companion object {
        @Volatile
        private var instance : UserRepository? = null

        fun getInstance(modelUserPreferences: UserModelPreferences, apiServices: ApiService): UserRepository = instance ?: synchronized(this) {
            instance ?: UserRepository(modelUserPreferences, apiServices)
        }.also { instance = it }
    }


}