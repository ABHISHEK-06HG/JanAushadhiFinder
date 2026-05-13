package com.mindmatrix.janaushadhifinder.ui.model

import com.mindmatrix.janaushadhifinder.data.local.entity.StoreEntity

data class StoreUIModel(
    val store: StoreEntity,
    val distanceMeters: Float,
    val formattedDistance: String,
    val isOpen: Boolean = true
)
