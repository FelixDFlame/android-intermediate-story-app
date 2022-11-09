package com.example.storyapp.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.model.DicodingStoryLoginResponse
import com.example.storyapp.utils.DataDummy
import com.example.storyapp.model.Result
import com.example.storyapp.repository.DicodingLoginRepository
import com.example.storyapp.utils.getOrAwaitValue
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var loginRepository: DicodingLoginRepository
    private lateinit var loginViewModel: LoginViewModel
    private val dummyLoginRequest = DataDummy.generateDummyLoginRequest()
    private val dummyLoginResponse = DataDummy.generateDummyLoginResponse()

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(loginRepository)
    }

    @Test
    fun `when Do Login Should Not Null and Return Success`() {
        val expectedLogin = MutableLiveData<Result<DicodingStoryLoginResponse>>()
        expectedLogin.value = Result.Success(dummyLoginResponse)

        `when`(loginRepository.doLogin(dummyLoginRequest)).thenReturn(expectedLogin)

        val actualLogin = loginViewModel.doLogin(dummyLoginRequest).getOrAwaitValue()
        Mockito.verify(loginRepository).doLogin(dummyLoginRequest)
        Assert.assertNotNull(actualLogin)
        Assert.assertTrue(actualLogin is Result.Success)
    }

    @Test
    fun `when Network Error Should Return Error`() {
        val expectedLogin = MutableLiveData<Result<DicodingStoryLoginResponse>>()
        expectedLogin.value = Result.Error("Error")

        `when`(loginRepository.doLogin(dummyLoginRequest)).thenReturn(expectedLogin)

        val actualLogin = loginViewModel.doLogin(dummyLoginRequest).getOrAwaitValue()
        Mockito.verify(loginRepository).doLogin(dummyLoginRequest)
        Assert.assertNotNull(actualLogin)
        Assert.assertTrue(actualLogin is Result.Error)
    }

}