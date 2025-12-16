package com.bazooka.inventory.data.repository

import com.bazooka.inventory.data.local.dao.ProjectPartDao
import com.bazooka.inventory.data.local.entity.ProjectPart
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProjectPartRepository @Inject constructor(
    private val projectPartDao: ProjectPartDao
) {
    fun getProjectParts(projectId: Long): Flow<List<ProjectPart>> =
        projectPartDao.getProjectParts(projectId)

    fun getPartProjects(bikePartId: Long): Flow<List<ProjectPart>> =
        projectPartDao.getPartProjects(bikePartId)

    suspend fun addPartToProject(projectId: Long, bikePartId: Long, quantity: Int) {
        val existing = projectPartDao.getProjectPartByIds(projectId, bikePartId)
        if (existing != null) {
            val updated = existing.copy(quantity = existing.quantity + quantity)
            projectPartDao.updateProjectPart(updated)
        } else {
            projectPartDao.insertProjectPart(
                ProjectPart(
                    projectId = projectId,
                    bikePartId = bikePartId,
                    quantity = quantity
                )
            )
        }
    }

    suspend fun removePartFromProject(projectId: Long, bikePartId: Long, quantity: Int? = null) {
        val existing = projectPartDao.getProjectPartByIds(projectId, bikePartId)
        if (existing != null) {
            if (quantity == null || quantity >= existing.quantity) {
                // Remove all or requested quantity is more than available
                projectPartDao.deleteProjectPartByIds(projectId, bikePartId)
            } else {
                // Reduce the quantity
                val updated = existing.copy(quantity = existing.quantity - quantity)
                projectPartDao.updateProjectPart(updated)
            }
        }
    }
}
