package com.cashflowtracker.miranda.utils

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.cashflowtracker.miranda.data.database.Transaction
import com.cashflowtracker.miranda.data.repositories.AccountsRepository
import com.cashflowtracker.miranda.data.repositories.NotificationsRepository
import com.cashflowtracker.miranda.data.repositories.RecurrencesRepository
import com.cashflowtracker.miranda.data.repositories.TransactionsRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.ZonedDateTime
import java.util.UUID

class RecurrenceUpdaterWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    private val notificationsRepo: NotificationsRepository by inject()
    private val recurrencesRepo: RecurrencesRepository by inject()
    private val transactionsRepo: TransactionsRepository by inject()
    private val accountsRepo: AccountsRepository by inject()

    //    private val userId = UUID.fromString(inputData.getString("userId"))
    private val recurrenceId = UUID.fromString(inputData.getString("recurrenceId"))

    override suspend fun doWork(): Result {
        return try {
            val oldRecurrence = recurrencesRepo.getByRecurrenceId(recurrenceId)
            val oldNotifications = notificationsRepo.getAllByRecurrenceId(recurrenceId)

            val transaction = Transaction(
                type = oldRecurrence.type,
                createdOn = oldRecurrence.reoccursOn,
                source = oldRecurrence.source,
                destination = oldRecurrence.destination,
                amount = oldRecurrence.amount,
                currency = oldRecurrence.currency,
                comment = oldRecurrence.comment,
                location = oldRecurrence.location,
                userId = oldRecurrence.userId,
                isCreatedByRecurrence = true
            )
            transactionsRepo.upsert(transaction)
            calculateBalance(
                transaction.amount,
                Currencies.valueOf(transaction.currency),
                transaction.type,
                transaction.source,
                transaction.destination,
                accountsRepo,
                transaction.userId
            )

            val recurrence = oldRecurrence.copy(
                reoccursOn = getRepeatTime(
                    ZonedDateTime.parse(oldRecurrence.reoccursOn),
                    Repeats.valueOf(oldRecurrence.type)
                )
            )
            recurrencesRepo.upsert(recurrence)

            oldNotifications.forEach {
                val notification = it.copy(
                    dateTime = getNotificationTime(
                        ZonedDateTime.parse(recurrence.reoccursOn),
                        Notifications.valueOf(it.notificationType)
                    )
                )
                notificationsRepo.upsert(notification)
            }

            val notifications = notificationsRepo.getAllByRecurrenceId(recurrenceId)
            notifications.forEach {
                scheduleNotification(it, recurrence, applicationContext)
            }

            // Schedule new recurrence

            Result.success()
        } catch (e: Exception) {
            Log.e("RecurrenceWorker", "Error processing recurrence", e)
            Result.failure()
        }
    }
}