package com.hikam.hikamstoryapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hikam.hikamstoryapp.data.UserDataRepository
import com.hikam.hikamstoryapp.data.di.Injection

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val repository: UserDataRepository) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(UserLoginViewModel::class.java) -> {
                UserLoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(CreateStoryViewModel::class.java) -> {
                CreateStoryViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java)->{
                MapsViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        fun clearInstance() {
            UserDataRepository.clearInstance()
            INSTANCE = null
        }

        fun getInstance(context: Context): ViewModelFactory {
            return INSTANCE ?: synchronized(this) {
                val instance = ViewModelFactory(Injection.provideRepository(context))
                INSTANCE = instance
                instance
            }
        }
    }
}
