package com.mindmatrix.janaushadhifinder.data.local.dao

import androidx.room.*
import com.mindmatrix.janaushadhifinder.data.local.entity.StoreEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface StoreDao {
    @Query("SELECT * FROM stores")
    fun getAllStores(): Flow<List<StoreEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStores(stores: List<StoreEntity>)

    @Query("SELECT * FROM stores WHERE id = :id")
    suspend fun getStoreById(id: Int): StoreEntity?

    @Query("SELECT COUNT(*) FROM stores")
    suspend fun getCount(): Int
}
