package com.mindmatrix.janaushadhifinder.data.repository

import com.mindmatrix.janaushadhifinder.data.local.dao.LocationDao
import com.mindmatrix.janaushadhifinder.data.local.entity.FavoriteLocationEntity
import kotlinx.coroutines.flow.Flow

class LocationRepository(private val locationDao: LocationDao) {
    fun getAllFavoriteLocations(): Flow<List<FavoriteLocationEntity>> =
        locationDao.getAllFavoriteLocations()

    suspend fun addLocation(location: FavoriteLocationEntity) =
        locationDao.insertLocation(location)

    suspend fun deleteLocation(location: FavoriteLocationEntity) =
        locationDao.deleteLocation(location)
}
