package com.bazooka.inventory.data.repository

import com.bazooka.inventory.data.local.dao.ProjectDao
import com.bazooka.inventory.data.local.entity.Project
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProjectRepository @Inject constructor(
    private val projectDao: ProjectDao
) {
    fun getAllProjects(): Flow<List<Project>> = projectDao.getAllProjects()

    fun getArchivedProjects(): Flow<List<Project>> = projectDao.getArchivedProjects()

    fun getProjectById(id: Long): Flow<Project?> = projectDao.getProjectById(id)

    fun searchProjects(query: String): Flow<List<Project>> =
        projectDao.searchProjects(query)

    suspend fun insertProject(project: Project): Long =
        projectDao.insertProject(project)

    suspend fun updateProject(project: Project) =
        projectDao.updateProject(project)

    suspend fun archiveProject(project: Project) {
        val archived = project.copy(isArchived = true)
        projectDao.updateProject(archived)
    }

    suspend fun unarchiveProject(project: Project) {
        val unarchived = project.copy(isArchived = false)
        projectDao.updateProject(unarchived)
    }

    suspend fun deleteProject(project: Project) =
        projectDao.deleteProject(project)

    suspend fun deleteProjectById(id: Long) =
        projectDao.deleteProjectById(id)

    fun getActiveProjectsCount(): Flow<Int> = projectDao.getActiveProjectsCount()

    fun getArchivedProjectsCount(): Flow<Int> = projectDao.getArchivedProjectsCount()
}
