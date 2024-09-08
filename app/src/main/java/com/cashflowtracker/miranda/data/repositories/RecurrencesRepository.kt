package com.cashflowtracker.miranda.data.repositories

import com.cashflowtracker.miranda.data.database.Recurrence
import com.cashflowtracker.miranda.data.database.RecurrencesDao
import java.util.UUID

class RecurrencesRepository(private val recurrencesDao: RecurrencesDao) {
    suspend fun upsert(recurrence: Recurrence) = recurrencesDao.upsert(recurrence)
    suspend fun delete(recurrence: Recurrence) = recurrencesDao.delete(recurrence)
    suspend fun deleteByRecurrenceId(recurrenceId: UUID) =
        recurrencesDao.deleteByRecurrenceId(recurrenceId)

    fun getAllByUserIdFlow(userId: UUID) = recurrencesDao.getAllByUserIdFlow(userId)
    fun getByRecurrenceId(recurrenceId: UUID) = recurrencesDao.getByRecurrenceId(recurrenceId)
    fun getByRecurrenceIdFlow(recurrenceId: UUID) =
        recurrencesDao.getByRecurrenceIdFlow(recurrenceId)

}