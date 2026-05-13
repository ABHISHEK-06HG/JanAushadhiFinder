package com.mindmatrix.janaushadhifinder.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mindmatrix.janaushadhifinder.data.model.RepeatType

@Entity(tableName = "reminders")
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val medicineName: String,
    val dosage: String,
    val notes: String, // e.g., "Before food", "After food"
    val repeatType: RepeatType,
    val repeatDays: String? = null, // Store as "1,3,5" for Monday, Wednesday, Friday
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "reminder_times")
data class ReminderTimeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val reminderId: Int,
    val hour: Int,
    val minute: Int
)
