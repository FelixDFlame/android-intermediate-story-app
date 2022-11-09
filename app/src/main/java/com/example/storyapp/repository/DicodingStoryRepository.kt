package com.example.storyapp.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.*
import com.example.storyapp.data.StoryItemRemoteMediator
import com.example.storyapp.database.StoryItem
import com.example.storyapp.database.StoryRoomDatabase
import com.example.storyapp.model.DicodingStoryBasicResponse
import com.example.storyapp.model.DicodingStoryStoriesResponse
import com.example.storyapp.model.Result
import com.example.storyapp.network.ApiConfig
import com.example.storyapp.network.ApiService
import com.google.gson.Gson
import retrofit2.HttpException
import java.lang.Exception

class DicodingStoryRepository(
    private val apiService: ApiService = ApiConfig.getApiService(),
    private val storyRoomDatabase: StoryRoomDatabase
) {

    private val TAG = "StoryRepo"

    fun getAllStory(token: String): LiveData<PagingData<StoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 20,
            ),
            remoteMediator = StoryItemRemoteMediator(storyRoomDatabase, apiService, token),
            pagingSourceFactory = {
                storyRoomDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

    fun getAllStoryGeo(token: String): LiveData<Result<DicodingStoryStoriesResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getCollectionStoryCallback("Bearer $token", 1, null, null)
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
        private var instance: DicodingStoryRepository? = null
        fun getInstance(context: Context): DicodingStoryRepository =
            instance ?: synchronized(this) {
                instance ?: DicodingStoryRepository(
                    apiService = ApiConfig.getApiService(),
                    storyRoomDatabase = StoryRoomDatabase.getDatabase(context)
                )
            }.also { instance = it }
    }
}