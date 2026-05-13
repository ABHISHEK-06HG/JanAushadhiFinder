package com.mindmatrix.janaushadhifinder.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mindmatrix.janaushadhifinder.data.local.entity.MedicineEntity
import com.mindmatrix.janaushadhifinder.data.repository.MedicineRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MedicineViewModel(private val repository: MedicineRepository) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    val categories: StateFlow<List<String>> = repository.getAllCategories()
        .map { listOf("All") + it }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), listOf("All"))

    val searchResults: StateFlow<List<MedicineEntity>> = combine(
        _searchQuery,
        _selectedCategory
    ) { query, category ->
        _isLoading.value = true
        // Simulating network delay for shimmer
        if (query.isNotEmpty()) kotlinx.coroutines.delay(500)
        
        val results = repository.searchMedicines(query, category).first()
        _isLoading.value = false
        results
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Recent searches (Mock)
    private val _recentSearches = MutableStateFlow(listOf("Paracetamol", "Metformin", "Telmisartan"))
    val recentSearches: StateFlow<List<String>> = _recentSearches

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateCategory(category: String) {
        _selectedCategory.value = category
    }

    suspend fun getMedicineById(id: Int): MedicineEntity? = repository.getMedicineById(id)

    fun calculateSavings(medicine: MedicineEntity): Double = medicine.priceBrand - medicine.priceGeneric
    
    fun calculateSavingsPercent(medicine: MedicineEntity): Int {
        if (medicine.priceBrand == 0.0) return 0
        return ((calculateSavings(medicine) / medicine.priceBrand) * 100).toInt()
    }

    class Factory(private val repository: MedicineRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MedicineViewModel(repository) as T
        }
    }
}
