package com.example.storyapp.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.example.storyapp.database.StoryItem
import com.example.storyapp.repository.DicodingStoryRepository
import com.example.storyapp.utils.DataDummy
import com.example.storyapp.utils.MainDispatcherRule
import com.example.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var dicodingStoryRepository: DicodingStoryRepository
    private lateinit var mainViewModel: MainViewModel
    private val dummyStoriesPagingResponse = DataDummy.generateDummyStoriesPagingResponse()
    private val dummyToken = DataDummy.generateDummyString()

    @Before
    fun setUp() {
        mainViewModel = MainViewModel(dicodingStoryRepository)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when Fetch Stories Should Not Null`() = runTest {
        val expectedStory = MutableLiveData<PagingData<StoryItem>>()
        expectedStory.value = dummyStoriesPagingResponse

        Mockito.`when`(dicodingStoryRepository.getAllStory(dummyToken)).thenReturn(expectedStory)

        val actualStory = mainViewModel.getAllStory(dummyToken).getOrAwaitValue()
        Mockito.verify(dicodingStoryRepository).getAllStory(dummyToken)
        Assert.assertNotNull(actualStory)
    }

}