package com.example.storyapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.storyapp.repository.DicodingStoryRepository

class MainViewModel(
    private val storyRepository: DicodingStoryRepository,
) : ViewModel() {
    fun getAllStory(token: String) = storyRepository.getAllStory(token).cachedIn(viewModelScope)
}