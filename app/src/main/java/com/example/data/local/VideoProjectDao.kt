package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.VideoProject
import kotlinx.coroutines.flow.Flow

@Dao
interface VideoProjectDao {
    @Query("SELECT * FROM video_projects ORDER BY createdAtTimestamp DESC")
    fun getAllProjects(): Flow<List<VideoProject>>

    @Query("SELECT * FROM video_projects WHERE isFavorite = 1 ORDER BY createdAtTimestamp DESC")
    fun getFavoriteProjects(): Flow<List<VideoProject>>

    @Query("SELECT * FROM video_projects WHERE id = :id")
    suspend fun getProjectById(id: Long): VideoProject?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: VideoProject): Long

    @Update
    suspend fun updateProject(project: VideoProject)

    @Delete
    suspend fun deleteProject(project: VideoProject)

    @Query("DELETE FROM video_projects WHERE id = :id")
    suspend fun deleteById(id: Long)
}
