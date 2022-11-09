package com.example.storyapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.storyapp.model.*
import com.example.storyapp.network.ApiConfig
import com.example.storyapp.network.ApiService
import com.google.gson.Gson
import retrofit2.HttpException
import java.lang.Exception

class DicodingLoginRepository(private val apiService: ApiService = ApiConfig.getApiService()) {
    private val TAG = "LoginRepo"

    fun doLogin(loginRequest: LoginRequest): LiveData<Result<DicodingStoryLoginResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.login(loginRequest)
                emit(Result.Success(response))
            } catch (e: HttpException) {
                val errorResponse = Gson().fromJson(
                    e.response()?.errorBody()?.charStream(),
                    DicodingStoryBasicResponse::class.java
                )
                var errorMessage = errorResponse.message
                if (errorMessage == null) {
                    errorMessage = e.message()
                }
                emit(Result.Error(errorMessage))
            } catch (e: Exception) {
                Log.e(TAG, "onFailure: ${e.message}")
                emit(Result.Error(e.message.toString()))
            }
        }

    companion object {
        @Volatile
        private var instance: DicodingLoginRepository? = null
        fun getInstance(): DicodingLoginRepository =
            instance ?: synchronized(this) {
                instance ?: DicodingLoginRepository()
            }.also { instance = it }
    }
}