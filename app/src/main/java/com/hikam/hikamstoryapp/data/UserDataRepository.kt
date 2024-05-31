package com.hikam.hikamstoryapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.hikam.hikamstoryapp.data.api.ApiService
import com.hikam.hikamstoryapp.response.ErrorResponse
import com.hikam.hikamstoryapp.response.ListStoryItem
import com.hikam.hikamstoryapp.response.LoginResponse
import com.hikam.hikamstoryapp.response.RegisterResponse
import com.hikam.hikamstoryapp.response.StoryUploadResponse
import com.hikam.hikamstoryapp.userpref.UserModel
import com.hikam.hikamstoryapp.userpref.UserPreferences
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File


class UserDataRepository(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences,
){

    fun uploadImage(imageFile: File, description: String): LiveData<ResultState<StoryUploadResponse>> = liveData{
        emit(ResultState.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        try {
            val successResponse = apiService.uploadImage(multipartBody, requestBody)
            emit(ResultState.Success(successResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, StoryUploadResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }
    }
    fun getStory(): LiveData<ResultState<List<ListStoryItem>>> = liveData{
        emit(ResultState.Loading)
        try{
            val response = apiService.getStory()
            emit(ResultState.Success(response.listStory))
        }catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }catch (e: Exception){
            emit(ResultState.Error(e.message ?: "Error"))
        }
    }

    fun login(email: String,password: String): LiveData<ResultState<LoginResponse>> = liveData{
        emit(ResultState.Loading)
        try {
            val response = apiService.login(email,password)
            emit(ResultState.Success(response))
        }catch (e:HttpException){
            val error = e.response()?.errorBody()?.string()
            val body = Gson().fromJson(error, ErrorResponse::class.java)
            emit(ResultState.Error(body.message))
        }
    }

    fun signup(name: String, email: String, password: String): LiveData<ResultState<RegisterResponse>> = liveData{
        emit(ResultState.Loading)
        try {
            val response = apiService.register(name,email,password)
            emit(ResultState.Success(response))
        }catch (e: HttpException){
            val error = e.response()?.errorBody()?.string()
            val body = Gson().fromJson(error, ErrorResponse::class.java)
            emit(ResultState.Error(body.message))
        }
    }

    fun getStoriesWithLocation(): LiveData<ResultState<List<ListStoryItem>>> = liveData{
        emit(ResultState.Loading)
        try{
            val response = apiService.getStoriesWithLocation()
            emit(ResultState.Success(response.listStory))
        }catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, ErrorResponse::class.java)
            emit(ResultState.Error(errorResponse.message))
        }catch (e: Exception){
            emit(ResultState.Error(e.message ?: "Error"))
        }
    }

    suspend fun saveSession(user: UserModel){
        userPreferences.saveSession(user)
    }
    fun getSession(): Flow<UserModel> {
        return userPreferences.getSession()
    }
    suspend fun logout(){
        userPreferences.logout()
    }

    companion object{
        private var INSTANCE: UserDataRepository? = null

        fun clearInstance(){
            INSTANCE = null
        }

        fun getInstance(
            apiService: ApiService,
            userPreferences: UserPreferences
        ): UserDataRepository =
            INSTANCE ?: synchronized(this){
                INSTANCE ?: UserDataRepository(apiService,userPreferences)
            }.also { INSTANCE = it }
    }
    fun getQuote(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
            }
        ).liveData
    }
}