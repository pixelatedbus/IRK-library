package com.irk.test.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.irk.test.model.LinkRepository
import com.irk.test.model.Material
import com.irk.test.model.ReferenceItem
import com.irk.test.model.BoyerMoore // Import the new standalone object
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

enum class MaterialFilter {
    ALL, ALGEO, MATDIS, STIMA
}
data class ReferenceUiState(
    val searchQuery: String = "",
    val selectedFilter: MaterialFilter = MaterialFilter.ALL,
    val filteredReferences: List<ReferenceItem> = LinkRepository.allReferences
)

class ReferenceViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ReferenceUiState())
    val uiState: StateFlow<ReferenceUiState> = _uiState.asStateFlow()

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        filterAndSearch()
    }

    fun onFilterChanged(filter: MaterialFilter) {
        _uiState.update { it.copy(selectedFilter = filter) }
        filterAndSearch()
    }

    private fun filterAndSearch() {
        viewModelScope.launch {
            val state = _uiState.value
            val query = state.searchQuery
            val filter = state.selectedFilter

            val results = if (query.isBlank()) {
                LinkRepository.allReferences
            } else {
                // Use the new, standalone Boyer-Moore implementation
                BoyerMoore.boyerMooreSearch(LinkRepository.allReferences, query)
            }

            val filteredResults = when (filter) {
                MaterialFilter.ALL -> results
                MaterialFilter.ALGEO -> results.filter { it.material == Material.ALGEO }
                MaterialFilter.MATDIS -> results.filter { it.material == Material.MATDIS }
                MaterialFilter.STIMA -> results.filter { it.material == Material.STIMA }
            }

            _uiState.update { it.copy(filteredReferences = filteredResults) }
        }
    }
}
