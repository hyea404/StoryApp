package com.submission.storyapp.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")
class UserModelPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun  saveSessionLogin(user: UserModel) {
        dataStore.edit {preferences ->
            preferences[EMAIL_KEY] = user.email
            preferences[TOKEN_KEY] = user.token
            preferences[IS_LOGIN_KEY] = true

        }
    }
    fun getSession(): Flow<UserModel>{
        return dataStore.data.map {preferences -> UserModel(
            email = preferences[EMAIL_KEY] ?: "",
            token = preferences[TOKEN_KEY] ?: "",
            isLogin = preferences[IS_LOGIN_KEY] ?: false

        )}
    }

    suspend fun logout() {
        dataStore.edit { preferences -> preferences.clear() }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserModelPreferences? = null
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")

        fun getInstance(dataStore: DataStore<Preferences>) : UserModelPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserModelPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}