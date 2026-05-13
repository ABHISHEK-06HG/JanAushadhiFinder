package com.mindmatrix.janaushadhifinder.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// FR-JAF-03: Store medicine data in Room DB
// Schema from SRD Section 8.1
@Entity(tableName = "medicine")
data class MedicineEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val brandName: String,       // NOT NULL — brand name of medicine
    val genericName: String,     // NOT NULL — generic/salt name
    val composition: String = "", // Composition details
    val category: String = "General", // Category for filtering
    val priceBrand: Double,      // NOT NULL — branded price in ₹
    val priceGeneric: Double,    // NOT NULL — generic price in ₹
    val createdAt: Long = System.currentTimeMillis()  // Unix timestamp
)
