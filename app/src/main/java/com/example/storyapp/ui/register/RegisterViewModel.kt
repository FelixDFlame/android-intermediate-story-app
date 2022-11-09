package com.example.storyapp.ui.register

import androidx.lifecycle.ViewModel
import com.example.storyapp.model.RegisterRequest
import com.example.storyapp.repository.DicodingRegisterRepository

class RegisterViewModel(private val dicodingRegisterRepository: DicodingRegisterRepository) :
    ViewModel() {

    fun doRegister(registerRequest: RegisterRequest) =
        dicodingRegisterRepository.doRegister(registerRequest)

}