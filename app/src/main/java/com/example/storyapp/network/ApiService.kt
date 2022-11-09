package com.example.storyapp.network

import com.example.storyapp.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @POST("/v1/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): DicodingStoryLoginResponse

    @POST("/v1/register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): DicodingStoryBasicResponse

    @Multipart
    @POST("/v1/stories")
    suspend fun addStory(
        @Header("Authorization") auth: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody? = null,
        @Part("lon") long: RequestBody? = null,
    ): DicodingStoryBasicResponse

    @GET("/v1/stories")
    suspend fun getCollectionStoryCallback(
        @Header("Authorization") auth: String,
        @Query("location") location: Int = 0,
        @Query("page") page: Int?,
        @Query("size") size: Int?,
    ): DicodingStoryStoriesResponse

}