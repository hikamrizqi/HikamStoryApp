package com.hikam.hikamstoryapp.userpref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name= "login")

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    suspend fun saveSession(user: UserModel) {
        dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = user.email
            preferences[PASSWORD_KEY] = user.password
            preferences[TOKEN_KEY] = user.token
            preferences[IS_LOGIN] = true
        }
    }

    suspend fun logout() {
        dataStore.edit { preferences ->
            preferences[TOKEN_KEY] = ""
            preferences[EMAIL_KEY] = ""
            preferences[PASSWORD_KEY] = ""
            preferences[IS_LOGIN] = false
        }
    }

    fun getSession(): Flow<UserModel> {
        return dataStore.data.map { preferences ->
            UserModel(
                preferences[TOKEN_KEY] ?: "",
                preferences[EMAIL_KEY] ?: "",
                preferences[PASSWORD_KEY] ?: "",
                preferences[IS_LOGIN] ?: false
            )
        }
    }

    companion object {
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val PASSWORD_KEY = stringPreferencesKey("password")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val IS_LOGIN = booleanPreferencesKey("isLogin")

        private var INSTANCE: UserPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}
