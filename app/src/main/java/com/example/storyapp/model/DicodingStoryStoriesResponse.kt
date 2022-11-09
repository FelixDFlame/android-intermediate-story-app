package com.example.storyapp.model

import com.example.storyapp.database.StoryItem

data class DicodingStoryStoriesResponse(
	val listStory: List<StoryItem>,
	val error: Boolean,
	val message: String
)

