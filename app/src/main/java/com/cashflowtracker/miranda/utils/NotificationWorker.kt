package com.cashflowtracker.miranda.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.cashflowtracker.miranda.data.repositories.AccountsRepository
import com.cashflowtracker.miranda.ui.screens.ViewRecurrence
import kotlinx.coroutines.CoroutineScope
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.java.KoinJavaComponent.inject
import java.util.UUID

class NotificationWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {
    private val accountsRepo: AccountsRepository by inject()

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


    override suspend fun doWork(): Result {
        val source = if ((recurrenceType == TransactionType.OUTPUT.name
                    || recurrenceType == TransactionType.TRANSFER.name)
            && source.isNotEmpty()
        ) {
            accountsRepo.getByAccountId(UUID.fromString(source)).title
        } else {
            formatSource(this.source, recurrenceType, applicationContext)
        }

        val destination = if ((recurrenceType == TransactionType.INPUT.name
                    || recurrenceType == TransactionType.TRANSFER.name)
            && destination.isNotEmpty()
        ) {
            accountsRepo.getByAccountId(UUID.fromString(destination)).title
        } else {
            formatDestination(this.destination, recurrenceType, applicationContext)
        }

        val intent = Intent(applicationContext, ViewRecurrence::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        intent.putExtra("recurrenceId", recurrenceId)

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        NotificationHelper.sendNotification(
            applicationContext,
            UUID.fromString(notificationId).hashCode(),
            notificationType,
            recurrenceType,
            source,
            destination,
            amount,
            Currencies.valueOf(currency),
            comment,
            iconFactory(recurrenceType, this.source, this.destination),
            pendingIntent
        )
        return Result.success()
    }
}