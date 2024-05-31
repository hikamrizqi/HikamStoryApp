package com.hikam.hikamstoryapp.viewmodel

import androidx.lifecycle.ViewModel
import com.hikam.hikamstoryapp.data.UserDataRepository
import com.hikam.hikamstoryapp.userpref.UserModel

class UserLoginViewModel(private val repository: UserDataRepository): ViewModel() {

    fun login(email: String, password: String) = repository.login(email,password)

    suspend fun saveSession(userModel: UserModel){
        repository.saveSession(userModel)
    }
}