package com.mindmatrix.janaushadhifinder.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mindmatrix.janaushadhifinder.data.local.entity.StoreEntity
import com.mindmatrix.janaushadhifinder.data.repository.StoreRepository
import com.mindmatrix.janaushadhifinder.ui.model.StoreUIModel
import com.mindmatrix.janaushadhifinder.util.LocationUtils
import kotlinx.coroutines.flow.*

class StoreViewModel(private val repository: StoreRepository) : ViewModel() {

    private val _userLocation = MutableStateFlow<Pair<Double, Double>?>(null)
    
    val nearbyStores: StateFlow<List<StoreUIModel>> = combine(
        repository.getAllStores(),
        _userLocation
    ) { stores, location ->
        if (location == null) {
            stores.map { 
                StoreUIModel(it, 0f, "Location unknown") 
            }
        } else {
            stores.map { store ->
                val distance = LocationUtils.calculateDistance(
                    location.first, location.second,
                    store.latitude, store.longitude
                )
                StoreUIModel(
                    store = store,
                    distanceMeters = distance,
                    formattedDistance = LocationUtils.formatDistance(distance)
                )
            }.sortedBy { it.distanceMeters }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateUserLocation(lat: Double, lng: Double) {
        _userLocation.value = Pair(lat, lng)
    }

    suspend fun getStoreById(id: Int) = repository.getStoreById(id)

    class Factory(private val repository: StoreRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return StoreViewModel(repository) as T
        }
    }
}
