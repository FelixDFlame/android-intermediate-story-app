package com.example.storyapp.model

data class DicodingStoryLoginResponse(
	val loginResult: LoginResult,
	val error: Boolean,
	val message: String
)

data class LoginResult(
	val name: String,
	val userId: String,
	val token: String
)

