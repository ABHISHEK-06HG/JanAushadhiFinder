package com.mindmatrix.janaushadhifinder.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stores")
data class StoreEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val phoneNumber: String,
    val openingTime: String,
    val closingTime: String,
    val isAvailable: Boolean = true,
    val state: String = "",
    val district: String = "",
    val city: String = "",
    val pincode: String = ""
)
