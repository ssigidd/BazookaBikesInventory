package com.bazooka.inventory.data.local.dao

import androidx.room.*
import com.bazooka.inventory.data.local.entity.Project
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {

    @Query("SELECT * FROM projects WHERE isArchived = 0 ORDER BY dateCreated DESC")
    fun getAllProjects(): Flow<List<Project>>

    @Query("SELECT * FROM projects WHERE isArchived = 1 ORDER BY dateCreated DESC")
    fun getArchivedProjects(): Flow<List<Project>>

    @Query("SELECT * FROM projects WHERE id = :id")
    fun getProjectById(id: Long): Flow<Project?>

    @Query("SELECT * FROM projects WHERE isArchived = 0 AND (name LIKE '%' || :searchQuery || '%' OR description LIKE '%' || :searchQuery || '%')")
    fun searchProjects(searchQuery: String): Flow<List<Project>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: Project): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProjects(projects: List<Project>)

    @Update
    suspend fun updateProject(project: Project)

    @Delete
    suspend fun deleteProject(project: Project)

    @Query("DELETE FROM projects WHERE id = :id")
    suspend fun deleteProjectById(id: Long)

    @Query("SELECT COUNT(*) FROM projects WHERE isArchived = 0")
    fun getActiveProjectsCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM projects WHERE isArchived = 1")
    fun getArchivedProjectsCount(): Flow<Int>
}
