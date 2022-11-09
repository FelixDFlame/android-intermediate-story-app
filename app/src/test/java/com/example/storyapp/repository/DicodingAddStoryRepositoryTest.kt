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
class DicodingAddStoryRepositoryTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var apiService: ApiService
    private lateinit var dicodingAddStoryRepository: DicodingAddStoryRepository

    private val dummyToken = DataDummy.generateDummyString()
    private val dummyImageMultipart = DataDummy.generateDummyMultipartbody()
    private val dummyDescription = DataDummy.getDummyRequestBody()
    private val dummyLat = DataDummy.getDummyRequestBody()
    private val dummyLon = DataDummy.getDummyRequestBody()
    private val dummyBasicResponse = DataDummy.generateDummyBasicResponse()

    @Before
    fun setUp() {
        apiService = FakeApiService()
        dicodingAddStoryRepository = DicodingAddStoryRepository(apiService)
    }

    @Test
    fun `when do Upload Should Not Null and Return Success Message`() = runTest {
        val expectedAddStory = Result.Success(dummyBasicResponse)
        val actualAddStory = dicodingAddStoryRepository.doUpload(
            dummyToken,
            dummyImageMultipart,
            dummyDescription,
            dummyLat,
            dummyLon
        )
        actualAddStory.observeForTesting {
            Assert.assertNotNull(actualAddStory)
            Assert.assertEquals(
                expectedAddStory.data.message,
                (actualAddStory.value as Result.Success).data.message
            )
        }
    }
}