package com.hikam.hikamstoryapp.viewmodel

import androidx.lifecycle.ViewModel
import com.hikam.hikamstoryapp.data.UserDataRepository
import java.io.File

class CreateStoryViewModel(private val repository: UserDataRepository) : ViewModel() {
    fun uploadImage(file: File, description: String) = repository.uploadImage(file, description)
}