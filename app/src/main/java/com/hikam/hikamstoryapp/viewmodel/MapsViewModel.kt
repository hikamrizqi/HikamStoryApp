package com.hikam.hikamstoryapp.viewmodel

import androidx.lifecycle.ViewModel
import com.hikam.hikamstoryapp.data.UserDataRepository

class MapsViewModel(private val repository: UserDataRepository) : ViewModel() {

    fun getStoriesWithLocation() = repository.getStoriesWithLocation()
}