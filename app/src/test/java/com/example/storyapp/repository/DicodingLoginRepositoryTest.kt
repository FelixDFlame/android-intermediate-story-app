package com.example.storyapp.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.storyapp.network.ApiService
import com.example.storyapp.utils.DataDummy
import com.example.storyapp.utils.MainDispatcherRule
import com.example.storyapp.utils.observeForTesting
import com.example.storyapp.model.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DicodingLoginRepositoryTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var apiService: ApiService
    private lateinit var dicodingLoginRepository: DicodingLoginRepository

    private val dummyLoginRequestBody = DataDummy.generateDummyLoginRequest()
    private val dummyLoginResponse = DataDummy.generateDummyLoginResponse()

    @Before
    fun setUp() {
        apiService = FakeApiService()
        dicodingLoginRepository = DicodingLoginRepository(apiService)
    }

    @Test
    fun `when do Login Should Not Null and Return Success Message`() = runTest {
        val expectedLogin = Result.Success(dummyLoginResponse)
        val actualLogin = dicodingLoginRepository.doLogin(dummyLoginRequestBody)
        actualLogin.observeForTesting {
            Assert.assertNotNull(actualLogin)
            Assert.assertEquals(
                expectedLogin.data.message,
                (actualLogin.value as Result.Success).data.message
            )
        }
    }

}