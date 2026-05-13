package com.mindmatrix.janaushadhifinder.data.local.dao

import androidx.room.*
import com.mindmatrix.janaushadhifinder.data.local.entity.MedicineEntity
import kotlinx.coroutines.flow.Flow

// CRITICAL (SRD §3.2): DAO never called from UI directly — only via Repository
@Dao
interface MedicineDao {

    // FR-JAF-01: Fuzzy search by brand name (handles small spelling mistakes via LIKE)
    @Query("SELECT * FROM medicine WHERE (brandName LIKE '%' || :query || '%' OR genericName LIKE '%' || :query || '%') AND (:category = 'All' OR category = :category) ORDER BY brandName ASC")
    fun searchMedicines(query: String, category: String = "All"): Flow<List<MedicineEntity>>

    @Query("SELECT DISTINCT category FROM medicine ORDER BY category ASC")
    fun getAllCategories(): Flow<List<String>>

    @Query("SELECT * FROM medicine ORDER BY brandName ASC")
    fun getAllMedicines(): Flow<List<MedicineEntity>>

    @Query("SELECT * FROM medicine WHERE id = :id")
    suspend fun getMedicineById(id: Int): MedicineEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(medicines: List<MedicineEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(medicine: MedicineEntity)

    @Query("SELECT COUNT(*) FROM medicine")
    suspend fun getCount(): Int
}
