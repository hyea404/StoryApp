package com.submission.storyapp.di

import android.content.Context
import com.submission.storyapp.data.preferences.UserModelPreferences
import com.submission.storyapp.data.preferences.dataStore
import com.submission.storyapp.data.retrofit.ApiConfig
import com.submission.storyapp.repository.UserRepository

object Injection {
    fun provideRepository(context: Context): UserRepository {
        val preference = UserModelPreferences.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(preference, apiService)
    }
}