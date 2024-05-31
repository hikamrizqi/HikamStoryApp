package com.hikam.hikamstoryapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.hikam.hikamstoryapp.data.UserDataRepository
import com.hikam.hikamstoryapp.response.ListStoryItem
import com.hikam.hikamstoryapp.userpref.UserModel

class MainViewModel(private val repository: UserDataRepository): ViewModel() {
    fun getStories() = repository.getStory()

    suspend fun logout(){
        repository.logout()
    }
    fun getSession(): LiveData<UserModel>{
        return repository.getSession().asLiveData()
    }

    val quote: LiveData<PagingData<ListStoryItem>> =
        repository.getQuote().cachedIn(viewModelScope)
}