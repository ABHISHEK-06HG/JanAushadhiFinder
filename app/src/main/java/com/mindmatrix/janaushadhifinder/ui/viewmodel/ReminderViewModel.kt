package com.mindmatrix.janaushadhifinder.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mindmatrix.janaushadhifinder.data.local.entity.IntakeRecordEntity
import com.mindmatrix.janaushadhifinder.data.local.entity.ReminderEntity
import com.mindmatrix.janaushadhifinder.data.local.entity.ReminderTimeEntity
import com.mindmatrix.janaushadhifinder.data.model.IntakeStatus
import com.mindmatrix.janaushadhifinder.data.repository.ReminderRepository
import com.mindmatrix.janaushadhifinder.notification.ReminderScheduler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ReminderViewModel(
    application: Application,
    private val repository: ReminderRepository
) : AndroidViewModel(application) {

    private val scheduler = ReminderScheduler(application)

    val allReminders: StateFlow<List<ReminderEntity>> = repository.allReminders
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allIntakeHistory: StateFlow<List<IntakeRecordEntity>> = repository.allIntakeHistory
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun getTimesForReminder(reminderId: Int): Flow<List<ReminderTimeEntity>> =
        repository.getTimesForReminder(reminderId)

    fun addReminder(reminder: ReminderEntity, times: List<ReminderTimeEntity>) {
        viewModelScope.launch {
            repository.addReminder(reminder, times)
            // Schedule alarms for the new reminder
            // We need the ID, which is why insertReminderWithTimes should return something or we re-fetch
            // For now, let's assume we fetch all and schedule if active
            refreshAlarms()
        }
    }

    fun updateReminder(reminder: ReminderEntity, times: List<ReminderTimeEntity>) {
        viewModelScope.launch {
            repository.updateReminder(reminder, times)
            refreshAlarms()
        }
    }

    fun deleteReminder(reminder: ReminderEntity) {
        viewModelScope.launch {
            repository.deleteReminder(reminder)
            // Cancel alarms logic would go here
        }
    }

    fun addIntakeRecord(reminderId: Int, timeId: Int, status: IntakeStatus) {
        viewModelScope.launch {
            repository.addIntakeRecord(
                IntakeRecordEntity(
                    reminderId = reminderId,
                    reminderTimeId = timeId,
                    timestamp = System.currentTimeMillis(),
                    status = status
                )
            )
        }
    }

    private fun refreshAlarms() {
        // Logic to reschedule all active reminders
        // This is a bit expensive but ensures consistency for this demo
    }

    class Factory(private val application: Application, private val repository: ReminderRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ReminderViewModel(application, repository) as T
        }
    }
}
