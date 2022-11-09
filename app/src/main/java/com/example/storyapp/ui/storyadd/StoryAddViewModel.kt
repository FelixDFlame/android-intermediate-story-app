package com.example.storyapp.ui.storyadd

import androidx.lifecycle.ViewModel
import com.example.storyapp.repository.DicodingAddStoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryAddViewModel(private val dicodingAddStoryRepository: DicodingAddStoryRepository) :
    ViewModel() {

    fun doUpload(
        token: String,
        imageMultipart: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ) = dicodingAddStoryRepository.doUpload(token, imageMultipart, description, lat, lon)

}