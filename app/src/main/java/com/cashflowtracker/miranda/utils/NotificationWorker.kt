package com.cashflowtracker.miranda.utils

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.UUID

class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {
    val notificationId = inputData.getString("notificationId") ?: ""
    val dateTime = inputData.getString("dateTime") ?: ""
    val notificationType = inputData.getString("notificationType") ?: ""
    val recurrenceId = inputData.getString("recurrenceId") ?: ""
    val userId = inputData.getString("userId") ?: ""
    val recurrenceType = inputData.getString("recurrenceType") ?: ""
    val source = inputData.getString("source") ?: ""
    val destination = inputData.getString("destination") ?: ""
    val amount = inputData.getDouble("amount", 0.0)
    val currency = inputData.getString("currency") ?: ""
    val comment = inputData.getString("comment") ?: ""


    override fun doWork(): Result {
        NotificationHelper.sendNotification(
            applicationContext,
            UUID.fromString(notificationId).hashCode(),
            notificationType,
            source,
            destination,
            amount,
            Currencies.valueOf(currency),
            comment,
            iconFactory(recurrenceType, source, destination)
        )
        return Result.success()
    }
}