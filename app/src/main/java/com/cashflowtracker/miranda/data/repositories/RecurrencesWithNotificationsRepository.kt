package com.cashflowtracker.miranda.data.repositories

import com.cashflowtracker.miranda.data.database.RecurrencesWithNotificationsDao
import java.util.UUID

class RecurrencesWithNotificationsRepository(private val recurrencesWithNotificationsDao: RecurrencesWithNotificationsDao) {
    fun getAllByUserId(userId: UUID) = recurrencesWithNotificationsDao.getAllByUserId(userId)
}