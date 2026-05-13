package com.mindmatrix.janaushadhifinder.data.repository

import com.mindmatrix.janaushadhifinder.data.local.dao.ReminderDao
import com.mindmatrix.janaushadhifinder.data.local.entity.IntakeRecordEntity
import com.mindmatrix.janaushadhifinder.data.local.entity.ReminderEntity
import com.mindmatrix.janaushadhifinder.data.local.entity.ReminderTimeEntity
import kotlinx.coroutines.flow.Flow

class ReminderRepository(private val reminderDao: ReminderDao) {

    val allReminders: Flow<List<ReminderEntity>> = reminderDao.getAllReminders()

    fun getTimesForReminder(reminderId: Int): Flow<List<ReminderTimeEntity>> =
        reminderDao.getTimesForReminder(reminderId)

    suspend fun addReminder(reminder: ReminderEntity, times: List<ReminderTimeEntity>) =
        reminderDao.insertReminderWithTimes(reminder, times)

    suspend fun updateReminder(reminder: ReminderEntity, times: List<ReminderTimeEntity>) =
        reminderDao.updateReminderWithTimes(reminder, times)

    suspend fun deleteReminder(reminder: ReminderEntity) =
        reminderDao.deleteReminder(reminder)

    // Intake Records
    suspend fun addIntakeRecord(record: IntakeRecordEntity) =
        reminderDao.insertIntakeRecord(record)

    fun getIntakeHistory(reminderId: Int): Flow<List<IntakeRecordEntity>> =
        reminderDao.getIntakeHistory(reminderId)

    val allIntakeHistory: Flow<List<IntakeRecordEntity>> = reminderDao.getAllIntakeHistory()
}
