package com.hikam.hikamstoryapp.data.di

import android.content.Context
import com.hikam.hikamstoryapp.data.UserDataRepository
import com.hikam.hikamstoryapp.data.api.ApiConfig
import com.hikam.hikamstoryapp.userpref.UserPreferences
import com.hikam.hikamstoryapp.userpref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserDataRepository {
        val pref = UserPreferences.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return UserDataRepository.getInstance(apiService,pref)
    }
}