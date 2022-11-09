package com.example.storyapp.ui.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.model.DicodingStoryBasicResponse
import com.example.storyapp.repository.DicodingRegisterRepository
import com.example.storyapp.utils.DataDummy
import com.example.storyapp.model.Result
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
class RegisterViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var registerRepository: DicodingRegisterRepository
    private lateinit var registerViewModel: RegisterViewModel
    private val dummyRegisterRequest = DataDummy.generateDummyRegisterRequest()
    private val dummyRegisterResponse = DataDummy.generateDummyBasicResponse()

    @Before
    fun setUp() {
        registerViewModel = RegisterViewModel(registerRepository)
    }

    @Test
    fun `when Do Register Should Not Null and Return Success`() {
        val expectedRegister = MutableLiveData<Result<DicodingStoryBasicResponse>>()
        expectedRegister.value = Result.Success(dummyRegisterResponse)

        `when`(registerRepository.doRegister(dummyRegisterRequest)).thenReturn(expectedRegister)

        val actualRegister = registerViewModel.doRegister(dummyRegisterRequest).getOrAwaitValue()
        Mockito.verify(registerRepository).doRegister(dummyRegisterRequest)
        Assert.assertNotNull(actualRegister)
        Assert.assertTrue(actualRegister is Result.Success)
    }

    @Test
    fun `when Network Error Should Return Error`() {
        val expectedRegister = MutableLiveData<Result<DicodingStoryBasicResponse>>()
        expectedRegister.value = Result.Error("Error")

        `when`(registerRepository.doRegister(dummyRegisterRequest)).thenReturn(expectedRegister)

        val actualRegister = registerViewModel.doRegister(dummyRegisterRequest).getOrAwaitValue()
        Mockito.verify(registerRepository).doRegister(dummyRegisterRequest)
        Assert.assertNotNull(actualRegister)
        Assert.assertTrue(actualRegister is Result.Error)
    }
}