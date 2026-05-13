package com.mindmatrix.janaushadhifinder.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mindmatrix.janaushadhifinder.data.model.IntakeStatus

@Entity(tableName = "intake_records")
data class IntakeRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val reminderId: Int,
    val reminderTimeId: Int,
    val timestamp: Long,
    val status: IntakeStatus
)
