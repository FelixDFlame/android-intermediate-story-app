package com.example.storyapp.ui.location

import androidx.lifecycle.ViewModel
import com.example.storyapp.repository.DicodingStoryRepository

class LocationViewModel(private val storyRepository: DicodingStoryRepository) : ViewModel() {
    fun fetchListStoryLocation(token: String) = storyRepository.getAllStoryGeo(token)
}