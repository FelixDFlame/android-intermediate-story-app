package com.example.storyapp.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.storyapp.model.Result
import com.example.storyapp.network.ApiService
import com.example.storyapp.utils.DataDummy
import com.example.storyapp.utils.MainDispatcherRule
import com.example.storyapp.utils.observeForTesting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DicodingRegisterRepositoryTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var apiService: ApiService
    private lateinit var dicodingRegisterRepository: DicodingRegisterRepository

    private val dummyRegisterRequestBody = DataDummy.generateDummyRegisterRequest()
    private val dummyBasicResponse = DataDummy.generateDummyBasicResponse()

    @Before
    fun setUp() {
        apiService = FakeApiService()
        dicodingRegisterRepository = DicodingRegisterRepository(apiService)
    }

    @Test
    fun `when do Register Should Not Null and Return Success Message`() = runTest {
        val expectedRegister = Result.Success(dummyBasicResponse)
        val actualRegister = dicodingRegisterRepository.doRegister(dummyRegisterRequestBody)
        actualRegister.observeForTesting {
            Assert.assertNotNull(actualRegister)
            Assert.assertEquals(
                expectedRegister.data.message,
                (actualRegister.value as Result.Success).data.message
            )
        }
    }

}