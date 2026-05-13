package com.mindmatrix.janaushadhifinder.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val medicineName = intent.getStringExtra("MEDICINE_NAME") ?: "Medicine"
        val reminderId = intent.getIntExtra("REMINDER_ID", 0)
        
        val notificationHelper = NotificationHelper(context)
        notificationHelper.showNotification(
            "Time for your medicine!",
            "Don't forget to take $medicineName",
            reminderId
        )
    }
}
