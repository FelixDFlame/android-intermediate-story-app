package com.example.storyapp.repository

import com.example.storyapp.model.*
import com.example.storyapp.network.ApiService
import com.example.storyapp.utils.DataDummy
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FakeApiService : ApiService {
    private val dummyLoginResponse = DataDummy.generateDummyLoginResponse()
    private val dummyBasicResponse = DataDummy.generateDummyBasicResponse()
    private val dummyStoriesResponse = DataDummy.generateDummyStoriesResponse()

    override suspend fun login(loginRequest: LoginRequest): DicodingStoryLoginResponse {
        return dummyLoginResponse
    }

    override suspend fun register(registerRequest: RegisterRequest): DicodingStoryBasicResponse {
        return dummyBasicResponse
    }

    override suspend fun addStory(
        auth: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        long: RequestBody?
    ): DicodingStoryBasicResponse {
        return dummyBasicResponse
    }

    override suspend fun getCollectionStoryCallback(
        auth: String,
        location: Int,
        page: Int?,
        size: Int?
    ): DicodingStoryStoriesResponse {
        return dummyStoriesResponse
    }
}