package com.hikam.hikamstoryapp.viewmodel

import androidx.lifecycle.ViewModel
import com.hikam.hikamstoryapp.data.UserDataRepository


class RegisterViewModel(private val repository: UserDataRepository): ViewModel() {
    fun register(name: String, email: String, password: String) =
        repository.signup(name,email, password)
}