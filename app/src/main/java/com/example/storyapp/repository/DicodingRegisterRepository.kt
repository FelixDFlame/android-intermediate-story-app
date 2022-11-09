package com.example.storyapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.storyapp.model.DicodingStoryBasicResponse
import com.example.storyapp.model.RegisterRequest
import com.example.storyapp.network.ApiConfig
import com.example.storyapp.network.ApiService
import com.example.storyapp.model.Result
import com.google.gson.Gson
import retrofit2.HttpException
import java.lang.Exception

class DicodingRegisterRepository(private val apiService: ApiService = ApiConfig.getApiService()) {
    private val TAG = "RegisterRepo"

    fun doRegister(registerRequest: RegisterRequest): LiveData<Result<DicodingStoryBasicResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val response = apiService.register(registerRequest)
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
        private var instance: DicodingRegisterRepository? = null
        fun getInstance(): DicodingRegisterRepository =
            instance ?: synchronized(this) {
                instance ?: DicodingRegisterRepository()
            }.also { instance = it }
    }
}