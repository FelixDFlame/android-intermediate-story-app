package com.example.storyapp.ui.location

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.model.DicodingStoryStoriesResponse
import com.example.storyapp.model.Result
import com.example.storyapp.repository.DicodingStoryRepository
import com.example.storyapp.utils.DataDummy
import com.example.storyapp.utils.getOrAwaitValue
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LocationViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var dicodingStoryRepository: DicodingStoryRepository
    private lateinit var locationViewModel: LocationViewModel
    private val dummyStoriesResponse = DataDummy.generateDummyStoriesResponse()
    private val dummyToken = DataDummy.generateDummyString()

    @Before
    fun setUp() {
        locationViewModel = LocationViewModel(dicodingStoryRepository)
    }

    @Test
    fun `when Fetch Stories Location Should Not Null and Return Success`() {
        val expectedStory = MutableLiveData<Result<DicodingStoryStoriesResponse>>()
        expectedStory.value = Result.Success(dummyStoriesResponse)

        Mockito.`when`(dicodingStoryRepository.getAllStoryGeo(dummyToken)).thenReturn(expectedStory)

        val actualStory = locationViewModel.fetchListStoryLocation(dummyToken).getOrAwaitValue()
        Mockito.verify(dicodingStoryRepository).getAllStoryGeo(dummyToken)
        Assert.assertNotNull(actualStory)
        Assert.assertTrue(actualStory is Result.Success)
        Assert.assertEquals(
            dummyStoriesResponse.listStory.size,
            (actualStory as Result.Success).data.listStory.size
        )
    }

    @Test
    fun `when Network Error Should Return Error`() {
        val expectedStory = MutableLiveData<Result<DicodingStoryStoriesResponse>>()
        expectedStory.value = Result.Error("Error")

        Mockito.`when`(dicodingStoryRepository.getAllStoryGeo(dummyToken)).thenReturn(expectedStory)

        val actualStory = locationViewModel.fetchListStoryLocation(dummyToken).getOrAwaitValue()
        Mockito.verify(dicodingStoryRepository).getAllStoryGeo(dummyToken)
        Assert.assertNotNull(actualStory)
        Assert.assertTrue(actualStory is Result.Error)
    }
}