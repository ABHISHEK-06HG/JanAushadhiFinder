package com.mindmatrix.janaushadhifinder.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mindmatrix.janaushadhifinder.data.local.entity.FavoriteLocationEntity
import com.mindmatrix.janaushadhifinder.data.repository.LocationRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LocationViewModel(private val repository: LocationRepository) : ViewModel() {

    private val _currentLocation = MutableStateFlow<FavoriteLocationEntity?>(
        FavoriteLocationEntity(name = "Mysuru (Default)", latitude = 12.2958, longitude = 76.6394)
    )
    val currentLocation: StateFlow<FavoriteLocationEntity?> = _currentLocation

    private val _selectedState = MutableStateFlow("Karnataka")
    val selectedState: StateFlow<String> = _selectedState

    private val _selectedCity = MutableStateFlow("Mysuru")
    val selectedCity: StateFlow<String> = _selectedCity

    val favoriteLocations: StateFlow<List<FavoriteLocationEntity>> = repository.getAllFavoriteLocations()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateCurrentLocation(location: FavoriteLocationEntity) {
        _currentLocation.value = location
    }

    fun selectState(state: String) {
        _selectedState.value = state
        // Reset city when state changes
        _selectedCity.value = "" 
    }

    fun selectCity(city: String) {
        _selectedCity.value = city
    }

    fun setLocationFromCoordinates(lat: Double, lng: Double, name: String) {
        _currentLocation.value = FavoriteLocationEntity(name = name, latitude = lat, longitude = lng)
    }

    fun addFavoriteLocation(location: FavoriteLocationEntity) {
        viewModelScope.launch {
            repository.addLocation(location)
        }
    }

    fun deleteFavoriteLocation(location: FavoriteLocationEntity) {
        viewModelScope.launch {
            repository.deleteLocation(location)
        }
    }

    class Factory(private val repository: LocationRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LocationViewModel(repository) as T
        }
    }
}
