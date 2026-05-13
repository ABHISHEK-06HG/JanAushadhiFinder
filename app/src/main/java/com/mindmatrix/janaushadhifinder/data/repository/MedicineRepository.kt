package com.mindmatrix.janaushadhifinder.data.repository

import com.mindmatrix.janaushadhifinder.data.local.dao.MedicineDao
import com.mindmatrix.janaushadhifinder.data.local.entity.MedicineEntity
import kotlinx.coroutines.flow.Flow

class MedicineRepository(private val medicineDao: MedicineDao) {

    fun searchMedicines(query: String, category: String = "All"): Flow<List<MedicineEntity>> =
        medicineDao.searchMedicines(query, category)

    fun getAllMedicines(): Flow<List<MedicineEntity>> = medicineDao.getAllMedicines()

    fun getAllCategories(): Flow<List<String>> = medicineDao.getAllCategories()

    suspend fun getMedicineById(id: Int): MedicineEntity? = medicineDao.getMedicineById(id)

    suspend fun insertAll(medicines: List<MedicineEntity>) = medicineDao.insertAll(medicines)
}
