package com.cashflowtracker.miranda.utils

import android.content.Context
import android.util.Log
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.cashflowtracker.miranda.data.database.Notification
import com.cashflowtracker.miranda.data.database.Recurrence
import java.time.Duration
import java.time.ZonedDateTime
import java.util.UUID
import java.util.concurrent.TimeUnit

fun scheduleNotification(notification: Notification, recurrence: Recurrence, context: Context) {
    val zdt = ZonedDateTime.parse(notification.dateTime)
    val now = ZonedDateTime.now()
    val delayMillis = Duration.between(now, zdt).toMillis()
    if (zdt.isBefore(now)) {
        return
    }
    Log.d("ScheduleNotification", "Now: $now, Notification Time: $zdt, Delay: $delayMillis")

    val data = Data.Builder()
        .putString("notificationId", notification.notificationId.toString())
        .putString("dateTime", notification.dateTime)
        .putString("notificationType", notification.notificationType)
        .putString("recurrenceId", notification.recurrenceId.toString())
        .putString("userId", notification.userId.toString())
        .putString("recurrenceType", recurrence.type)
        .putString("source", recurrence.source)
        .putString("destination", recurrence.destination)
        .putDouble("amount", recurrence.amount)
        .putString("currency", recurrence.currency)
        .putString("comment", recurrence.comment)
        .build()

    val notificationRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
        .setInputData(data)
        .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
        .addTag(recurrence.recurrenceId.toString())
        .build()
    WorkManager.getInstance(context).enqueue(notificationRequest)
}

fun cancelScheduledNotifications(recurrenceId: UUID, context: Context) {
    WorkManager.getInstance(context).cancelAllWorkByTag(recurrenceId.toString())
}