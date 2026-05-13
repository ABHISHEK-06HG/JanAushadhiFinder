package com.mindmatrix.janaushadhifinder.data.local.dao

import androidx.room.*
import com.mindmatrix.janaushadhifinder.data.local.entity.ReminderEntity
import com.mindmatrix.janaushadhifinder.data.local.entity.ReminderTimeEntity
import com.mindmatrix.janaushadhifinder.data.local.entity.IntakeRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {

    @Query("SELECT * FROM reminders ORDER BY createdAt DESC")
    fun getAllReminders(): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminder_times WHERE reminderId = :reminderId")
    fun getTimesForReminder(reminderId: Int): Flow<List<ReminderTimeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: ReminderEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminderTime(reminderTime: ReminderTimeEntity)

    @Update
    suspend fun updateReminder(reminder: ReminderEntity)

    @Delete
    suspend fun deleteReminder(reminder: ReminderEntity)

    @Query("DELETE FROM reminder_times WHERE reminderId = :reminderId")
    suspend fun deleteTimesForReminder(reminderId: Int)

    @Transaction
    suspend fun insertReminderWithTimes(reminder: ReminderEntity, times: List<ReminderTimeEntity>) {
        val id = insertReminder(reminder)
        times.forEach { insertReminderTime(it.copy(reminderId = id.toInt())) }
    }

    @Transaction
    suspend fun updateReminderWithTimes(reminder: ReminderEntity, times: List<ReminderTimeEntity>) {
        updateReminder(reminder)
        deleteTimesForReminder(reminder.id)
        times.forEach { insertReminderTime(it.copy(reminderId = reminder.id)) }
    }

    // Intake Records
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIntakeRecord(record: IntakeRecordEntity)

    @Query("SELECT * FROM intake_records WHERE reminderId = :reminderId ORDER BY timestamp DESC")
    fun getIntakeHistory(reminderId: Int): Flow<List<IntakeRecordEntity>>

    @Query("SELECT * FROM intake_records ORDER BY timestamp DESC")
    fun getAllIntakeHistory(): Flow<List<IntakeRecordEntity>>
}
