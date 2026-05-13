package com.mindmatrix.janaushadhifinder.data.local.dao

import androidx.room.*
import com.mindmatrix.janaushadhifinder.data.local.entity.FavoriteLocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {
    @Query("SELECT * FROM favorite_locations")
    fun getAllFavoriteLocations(): Flow<List<FavoriteLocationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: FavoriteLocationEntity)

    @Delete
    suspend fun deleteLocation(location: FavoriteLocationEntity)

    @Query("SELECT COUNT(*) FROM favorite_locations")
    suspend fun getCount(): Int
}
