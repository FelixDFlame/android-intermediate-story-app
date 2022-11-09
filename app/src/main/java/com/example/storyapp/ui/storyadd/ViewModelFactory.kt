package com.example.storyapp.ui.storyadd

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.repository.DicodingAddStoryRepository

class ViewModelFactory private constructor(private val dicodingAddStoryRepository: DicodingAddStoryRepository) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryAddViewModel::class.java)) {
            return StoryAddViewModel(dicodingAddStoryRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(DicodingAddStoryRepository.getInstance())
            }.also { instance = it }
    }
}