package com.bazooka.inventory.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bazooka.inventory.data.local.entity.BikePart
import com.bazooka.inventory.data.local.entity.Project
import com.bazooka.inventory.data.repository.BikePartRepository
import com.bazooka.inventory.data.repository.ProjectPartRepository
import com.bazooka.inventory.data.repository.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProjectUiState(
    val projects: List<Project> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val showArchived: Boolean = false
)

data class ProjectDetailUiState(
    val project: Project? = null,
    val bikeParts: List<ProjectPartWithDetails> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class ProjectPartWithDetails(
    val bikePart: BikePart,
    val quantityInProject: Int
)

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val projectRepository: ProjectRepository,
    private val bikePartRepository: BikePartRepository,
    private val projectPartRepository: ProjectPartRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _showArchived = MutableStateFlow(false)
    val showArchived: StateFlow<Boolean> = _showArchived.asStateFlow()

    private val _uiState = MutableStateFlow(ProjectUiState(isLoading = true))
    val uiState: StateFlow<ProjectUiState> = combine(
        _searchQuery,
        _showArchived,
        projectRepository.getAllProjects(),
        projectRepository.getArchivedProjects()
    ) { query, showArchived, activeProjects, archivedProjects ->
        val projects = if (showArchived) archivedProjects else activeProjects
        val filteredProjects = if (query.isNotBlank()) {
            projects.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.description.contains(query, ignoreCase = true)
            }
        } else {
            projects
        }
        ProjectUiState(
            projects = filteredProjects,
            isLoading = false,
            searchQuery = query,
            showArchived = showArchived
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProjectUiState(isLoading = true)
    )

    fun getProjectById(id: Long): Flow<Project?> {
        return projectRepository.getProjectById(id)
    }

    fun getProjectWithParts(projectId: Long): Flow<ProjectDetailUiState> {
        return combine(
            projectRepository.getProjectById(projectId),
            projectPartRepository.getProjectParts(projectId)
        ) { project, projectParts ->
            val partsWithDetails = projectParts.mapNotNull { projectPart ->
                bikePartRepository.getBikePartById(projectPart.bikePartId).first()?.let { bikePart ->
                    ProjectPartWithDetails(
                        bikePart = bikePart,
                        quantityInProject = projectPart.quantity
                    )
                }
            }
            ProjectDetailUiState(
                project = project,
                bikeParts = partsWithDetails,
                isLoading = false
            )
        }
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleShowArchived() {
        _showArchived.value = !_showArchived.value
    }

    fun insertProject(project: Project) {
        viewModelScope.launch {
            projectRepository.insertProject(project)
        }
    }

    fun updateProject(project: Project) {
        viewModelScope.launch {
            projectRepository.updateProject(project)
        }
    }

    fun archiveProject(project: Project) {
        viewModelScope.launch {
            projectRepository.archiveProject(project)
        }
    }

    fun unarchiveProject(project: Project) {
        viewModelScope.launch {
            projectRepository.unarchiveProject(project)
        }
    }

    fun deleteProject(project: Project) {
        viewModelScope.launch {
            projectRepository.deleteProject(project)
        }
    }

    fun addPartToProject(projectId: Long, bikePartId: Long, quantity: Int) {
        viewModelScope.launch {
            projectPartRepository.addPartToProject(projectId, bikePartId, quantity)
        }
    }

    fun removePartFromProject(projectId: Long, bikePartId: Long, quantity: Int? = null) {
        viewModelScope.launch {
            projectPartRepository.removePartFromProject(projectId, bikePartId, quantity)
        }
    }
}
