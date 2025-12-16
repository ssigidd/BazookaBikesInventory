package com.bazooka.inventory.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bazooka.inventory.data.local.entity.BikePart
import com.bazooka.inventory.data.repository.BikePartRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class BikePartUiState(
    val bikeParts: List<BikePart> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val selectedCategory: String? = null,
    val totalQuantity: Int = 0
)

@HiltViewModel
class BikePartViewModel @Inject constructor(
    private val repository: BikePartRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _uiState = MutableStateFlow(BikePartUiState(isLoading = true))
    val uiState: StateFlow<BikePartUiState> = combine(
        _searchQuery,
        _selectedCategory,
        repository.getAllBikeParts()
    ) { query, category, allParts ->
        val filteredParts = when {
            query.isNotBlank() -> allParts.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.description.contains(query, ignoreCase = true)
            }
            category != null -> allParts.filter { it.category == category }
            else -> allParts
        }
        BikePartUiState(
            bikeParts = filteredParts,
            isLoading = false,
            searchQuery = query,
            selectedCategory = category,
            totalQuantity = filteredParts.sumOf { it.quantity }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BikePartUiState(isLoading = true)
    )

    fun getLowStockParts(): Flow<List<BikePart>> {
        return repository.getLowStockBikeParts()
    }

    fun getBikePartById(id: Long): Flow<BikePart?> {
        return repository.getBikePartById(id)
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: String?) {
        _selectedCategory.value = category
    }

    fun insertBikePart(bikePart: BikePart) {
        viewModelScope.launch {
            repository.insertBikePart(bikePart)
        }
    }

    fun updateBikePart(bikePart: BikePart) {
        viewModelScope.launch {
            repository.updateBikePart(bikePart)
        }
    }

    fun deleteBikePart(bikePart: BikePart) {
        viewModelScope.launch {
            repository.deleteBikePart(bikePart)
        }
    }

    fun getCategories(): List<String> {
        return listOf(
            "Frame",
            "Fork",
            "Wheels",
            "Tires",
            "Brakes",
            "Drivetrain",
            "Handlebars",
            "Saddle",
            "Pedals",
            "Suspension",
            "Electronics",
            "Accessories",
            "Other"
        )
    }
}
