package com.example.storyapp.ui.login

import androidx.lifecycle.ViewModel
import com.example.storyapp.model.LoginRequest
import com.example.storyapp.repository.DicodingLoginRepository

class LoginViewModel(private val dicodingLoginRepository: DicodingLoginRepository) : ViewModel() {

    fun doLogin(loginRequest: LoginRequest) = dicodingLoginRepository.doLogin(loginRequest)
}