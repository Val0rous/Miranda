package com.cashflowtracker.miranda.data.repositories

import com.cashflowtracker.miranda.data.database.Notification
import com.cashflowtracker.miranda.data.database.NotificationsDao
import java.util.UUID

class NotificationsRepository(private val notificationsDao: NotificationsDao) {
    suspend fun upsert(notification: Notification) = notificationsDao.upsert(notification)
    suspend fun delete(notification: Notification) = notificationsDao.delete(notification)
    fun getAllByUserIdFlow(userId: UUID) = notificationsDao.getAllByUserIdFlow(userId)
    fun getAllByRecurrenceId(recurrenceId: UUID) =
        notificationsDao.getAllByRecurrenceId(recurrenceId)

    fun getAllByRecurrenceIdFlow(recurrenceId: UUID) =
        notificationsDao.getAllByRecurrenceIdFlow(recurrenceId)
}