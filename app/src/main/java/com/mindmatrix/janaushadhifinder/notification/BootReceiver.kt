package com.mindmatrix.janaushadhifinder.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mindmatrix.janaushadhifinder.data.local.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val db = AppDatabase.getInstance(context)
            val scheduler = ReminderScheduler(context)
            
            CoroutineScope(Dispatchers.IO).launch {
                val reminders = db.reminderDao().getAllReminders().first()
                reminders.filter { it.isActive }.forEach { reminder ->
                    val times = db.reminderDao().getTimesForReminder(reminder.id).first()
                    times.forEach { time ->
                        scheduler.scheduleReminder(reminder, time)
                    }
                }
            }
        }
    }
}
