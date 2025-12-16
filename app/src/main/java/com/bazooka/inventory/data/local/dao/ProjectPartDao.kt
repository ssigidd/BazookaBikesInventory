package com.bazooka.inventory.data.local.dao

import androidx.room.*
import com.bazooka.inventory.data.local.entity.ProjectPart
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectPartDao {

    @Query("SELECT * FROM project_parts WHERE projectId = :projectId")
    fun getProjectParts(projectId: Long): Flow<List<ProjectPart>>

    @Query("SELECT * FROM project_parts WHERE bikePartId = :bikePartId")
    fun getPartProjects(bikePartId: Long): Flow<List<ProjectPart>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProjectPart(projectPart: ProjectPart): Long

    @Update
    suspend fun updateProjectPart(projectPart: ProjectPart)

    @Delete
    suspend fun deleteProjectPart(projectPart: ProjectPart)

    @Query("DELETE FROM project_parts WHERE projectId = :projectId AND bikePartId = :bikePartId")
    suspend fun deleteProjectPartByIds(projectId: Long, bikePartId: Long)

    @Query("SELECT * FROM project_parts WHERE projectId = :projectId AND bikePartId = :bikePartId")
    suspend fun getProjectPartByIds(projectId: Long, bikePartId: Long): ProjectPart?
}
