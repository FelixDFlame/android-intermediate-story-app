package com.example.storyapp.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.storyapp.database.StoryRoomDatabase
import com.example.storyapp.model.Result
import com.example.storyapp.network.ApiService
import com.example.storyapp.utils.DataDummy
import com.example.storyapp.utils.MainDispatcherRule
import com.example.storyapp.utils.observeForTesting
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DicodingStoryRepositoryTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var storyRoomDatabase: StoryRoomDatabase
    private lateinit var apiService: ApiService
    private lateinit var dicodingStoryRepository: DicodingStoryRepository

    private val dummyToken = DataDummy.generateDummyString()
    private val dummyStoriesResponse = DataDummy.generateDummyStoriesResponse()

    @Before
    fun setUp() {
        apiService = FakeApiService()
        dicodingStoryRepository = DicodingStoryRepository(apiService, storyRoomDatabase)
    }

    @Test
    fun `when Fetch Story Location Should Not Null`(): Unit = runTest {
        val expectedStory = Result.Success(dummyStoriesResponse)
        val actualStory = dicodingStoryRepository.getAllStoryGeo(dummyToken)
        actualStory.observeForTesting {
            Assert.assertNotNull(actualStory)
            Assert.assertEquals(
                expectedStory.data.listStory.size,
                (actualStory.value as Result.Success).data.listStory.size
            )
        }
    }

    @Test
    fun `when Fetch Story Should Not Null`(): Unit = runTest {
        val actualStory = dicodingStoryRepository.getAllStoryGeo(dummyToken)
        actualStory.observeForTesting {
            Assert.assertNotNull(actualStory)
        }
    }

}