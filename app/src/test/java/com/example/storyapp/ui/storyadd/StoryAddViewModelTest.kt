package com.example.storyapp.ui.storyadd

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.model.DicodingStoryBasicResponse
import com.example.storyapp.model.Result
import com.example.storyapp.repository.DicodingAddStoryRepository
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
class StoryAddViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var addStoryRepository: DicodingAddStoryRepository
    private lateinit var storyAddViewModel: StoryAddViewModel
    private val dummyToken = DataDummy.generateDummyString()
    private val dummyImageMultipart = DataDummy.generateDummyMultipartbody()
    private val dummyDescription = DataDummy.getDummyRequestBody()
    private val dummyLat = DataDummy.getDummyRequestBody()
    private val dummyLon = DataDummy.getDummyRequestBody()

    private val dummyStoryAddResponse = DataDummy.generateDummyBasicResponse()

    @Before
    fun setUp() {
        storyAddViewModel = StoryAddViewModel(addStoryRepository)
    }

    @Test
    fun `when Do Upload Should Not Null and Return Success`() {
        val expectedAddStory = MutableLiveData<Result<DicodingStoryBasicResponse>>()
        expectedAddStory.value = Result.Success(dummyStoryAddResponse)

        Mockito.`when`(
            addStoryRepository.doUpload(
                dummyToken,
                dummyImageMultipart,
                dummyDescription,
                dummyLat,
                dummyLon
            )
        )
            .thenReturn(expectedAddStory)

        val actualAddStory = storyAddViewModel.doUpload(
            dummyToken,
            dummyImageMultipart,
            dummyDescription,
            dummyLat,
            dummyLon
        ).getOrAwaitValue()

        Mockito.verify(addStoryRepository).doUpload(
            dummyToken,
            dummyImageMultipart,
            dummyDescription,
            dummyLat,
            dummyLon
        )
        Assert.assertNotNull(actualAddStory)
        Assert.assertTrue(actualAddStory is Result.Success)
    }

    @Test
    fun `when Network Error Should Return Error`() {
        val expectedAddStory = MutableLiveData<Result<DicodingStoryBasicResponse>>()
        expectedAddStory.value = Result.Error("Error")

        Mockito.`when`(
            addStoryRepository.doUpload(
                dummyToken,
                dummyImageMultipart,
                dummyDescription,
                dummyLat,
                dummyLon
            )
        )
            .thenReturn(expectedAddStory)

        val actualAddStory = storyAddViewModel.doUpload(
            dummyToken,
            dummyImageMultipart,
            dummyDescription,
            dummyLat,
            dummyLon
        ).getOrAwaitValue()
        Mockito.verify(addStoryRepository).doUpload(
            dummyToken,
            dummyImageMultipart,
            dummyDescription,
            dummyLat,
            dummyLon
        )
        Assert.assertNotNull(actualAddStory)
        Assert.assertTrue(actualAddStory is Result.Error)
    }

}