package com.cashflowtracker.miranda.utils

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.UUID

class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {
    private val notificationId = inputData.getString("notificationId") ?: ""
    private val dateTime = inputData.getString("dateTime") ?: ""
    private val notificationType = inputData.getString("notificationType") ?: ""
    private val recurrenceId = inputData.getString("recurrenceId") ?: ""
    private val userId = inputData.getString("userId") ?: ""
    private val recurrenceType = inputData.getString("recurrenceType") ?: ""
    private val source = inputData.getString("source") ?: ""
    private val destination = inputData.getString("destination") ?: ""
    private val amount = inputData.getDouble("amount", 0.0)
    private val currency = inputData.getString("currency") ?: ""
    private val comment = inputData.getString("comment") ?: ""


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