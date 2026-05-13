package com.mindmatrix.janaushadhifinder.notification

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.mindmatrix.janaushadhifinder.data.local.entity.ReminderEntity
import com.mindmatrix.janaushadhifinder.data.local.entity.ReminderTimeEntity
import java.util.*

class ReminderScheduler(private val context: Context) {
    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun scheduleReminder(reminder: ReminderEntity, time: ReminderTimeEntity) {
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("MEDICINE_NAME", reminder.medicineName)
            putExtra("REMINDER_ID", reminder.id + time.id) // Unique ID for each time
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminder.id + time.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, time.hour)
            set(Calendar.MINUTE, time.minute)
            set(Calendar.SECOND, 0)
            if (before(Calendar.getInstance())) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        // For simplicity, scheduling as daily for now if not custom
        // In a real app, logic for repeatType would go here
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }

    fun cancelReminder(reminder: ReminderEntity, time: ReminderTimeEntity) {
        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminder.id + time.id,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        pendingIntent?.let {
            alarmManager.cancel(it)
        }
    }
}
