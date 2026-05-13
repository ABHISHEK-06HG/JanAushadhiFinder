package com.mindmatrix.janaushadhifinder.data.repository

import com.mindmatrix.janaushadhifinder.data.local.dao.StoreDao
import com.mindmatrix.janaushadhifinder.data.local.entity.StoreEntity
import kotlinx.coroutines.flow.Flow

class StoreRepository(private val storeDao: StoreDao) {
    fun getAllStores(): Flow<List<StoreEntity>> = storeDao.getAllStores()

    suspend fun insertStores(stores: List<StoreEntity>) = storeDao.insertStores(stores)

    suspend fun getStoreById(id: Int): StoreEntity? = storeDao.getStoreById(id)
}
