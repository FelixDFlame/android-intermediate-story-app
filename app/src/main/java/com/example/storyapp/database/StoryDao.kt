package com.example.storyapp.database

import androidx.paging.PagingSource
import androidx.room.*

@Dao
interface StoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(story: StoryItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStories(listStory: List<StoryItem>)

    @Delete
    fun delete(story: StoryItem)

    @Query("DELETE FROM StoryItem")
    suspend fun deleteAll()

    @Query("SELECT * from StoryItem ORDER BY id DESC")
    fun getAllStoryNoPaging(): List<StoryItem>

    @Query("SELECT * from StoryItem")
    fun getAllStory(): PagingSource<Int, StoryItem>
}