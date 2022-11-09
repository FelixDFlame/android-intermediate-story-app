package com.example.storyapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.storyapp.model.Result
import androidx.lifecycle.liveData
import com.example.storyapp.model.DicodingStoryBasicResponse
import com.example.storyapp.network.ApiConfig
import com.example.storyapp.network.ApiService
import com.google.gson.Gson
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class DicodingAddStoryRepository(private val apiService: ApiService = ApiConfig.getApiService()) {
    private val TAG = "AddStoryRepo"

    fun doUpload(
        token: String,
        imageMultipart: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): LiveData<Result<DicodingStoryBasicResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService
                .addStory(
                    auth = "Bearer $token",
                    file = imageMultipart,
                    description = description,
                    lat,
                    lon
                )
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
        private var instance: DicodingAddStoryRepository? = null
        fun getInstance(): DicodingAddStoryRepository =
            instance ?: synchronized(this) {
                instance ?: DicodingAddStoryRepository()
            }.also { instance = it }
    }

}