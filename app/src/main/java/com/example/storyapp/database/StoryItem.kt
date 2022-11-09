package com.example.storyapp.database

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class StoryItem(
    @PrimaryKey()
    @ColumnInfo(name = "id")
    var id: String = "",

    @ColumnInfo(name = "photo_url")
    var photoUrl: String = "",

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "created_at")
    var createdAt: String = "",

    @ColumnInfo(name = "description")
    var description: String = "",

    @ColumnInfo(name = "lon")
    var lon: Double = 0.0,

    @ColumnInfo(name = "lat")
    var lat: Double = 0.0,

    ) : Parcelable