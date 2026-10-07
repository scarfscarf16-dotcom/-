package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "video_projects")
data class VideoProject(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val surahNumber: Int,
    val surahName: String,
    val fromAyah: Int,
    val toAyah: Int,
    val reciterId: String,
    val reciterName: String,
    val backgroundPresetId: String,
    val durationSeconds: Int,
    val aspectRatio: String = "9:16",
    val videoFilePath: String? = null,
    val createdAtTimestamp: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false
)
