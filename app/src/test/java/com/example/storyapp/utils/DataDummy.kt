package com.example.storyapp.utils

import androidx.paging.PagingData
import com.example.storyapp.database.StoryItem
import com.example.storyapp.model.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

object DataDummy {
    fun generateDummyLoginRequest(): LoginRequest {
        return LoginRequest("XXXXXX", "xx@gmail.com")
    }

    fun generateDummyLoginResponse(): DicodingStoryLoginResponse {
        val loginResult = LoginResult(name = "XXXX", userId = "1234", token = "1234567890qwerty")
        return DicodingStoryLoginResponse(
            error = false,
            message = "Success",
            loginResult = loginResult
        )
    }

    fun generateDummyRegisterRequest(): RegisterRequest {
        return RegisterRequest("XXXXXXXX", "xxx", "xx@gmail.com")
    }

    fun generateDummyBasicResponse(): DicodingStoryBasicResponse {
        return DicodingStoryBasicResponse(
            error = false,
            message = "Success",
        )
    }

    fun generateDummyStoriesResponse(): DicodingStoryStoriesResponse {
        var listStory = listOf<StoryItem>()
        return DicodingStoryStoriesResponse(
            error = false,
            message = "Story fetch",
            listStory = listStory
        )
    }

    fun generateDummyString(): String {
        return "1234567890qwerty"
    }

    fun generateDummyMultipartbody(): MultipartBody.Part {
        val file = File("Image1")
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(
            "photo",
            "img1",
            requestImageFile
        )
    }

    fun getDummyRequestBody(): RequestBody {
        return "xxx".toString()
            .toRequestBody("text/plain".toMediaType())
    }

    fun generateDummyStoriesPagingResponse(): PagingData<StoryItem> {
        return PagingData.empty()
    }
}