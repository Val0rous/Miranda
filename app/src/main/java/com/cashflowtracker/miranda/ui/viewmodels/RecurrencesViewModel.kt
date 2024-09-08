package com.cashflowtracker.miranda.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cashflowtracker.miranda.data.database.Recurrence
import com.cashflowtracker.miranda.data.repositories.RecurrencesRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID

interface RecurrencesActions {
    fun addRecurrence(recurrence: Recurrence): Job
    fun updateRecurrence(recurrence: Recurrence): Job
    fun removeRecurrence(recurrence: Recurrence): Job
    fun removeRecurrence(recurrenceId: UUID): Job
    fun getAllByUserIdFlow(userId: UUID): Flow<List<Recurrence>>
    fun getByRecurrenceId(recurrenceId: UUID): Recurrence
    fun getByRecurrenceIdFlow(recurrenceId: UUID): Flow<Recurrence>
}

class RecurrencesViewModel(private val repository: RecurrencesRepository) : ViewModel() {
    val actions = object : RecurrencesActions {
        override fun addRecurrence(recurrence: Recurrence) = viewModelScope.launch {
            repository.upsert(recurrence)
        }

        override fun updateRecurrence(recurrence: Recurrence) = addRecurrence(recurrence)

        override fun removeRecurrence(recurrence: Recurrence) = viewModelScope.launch {
            repository.delete(recurrence)
        }

        override fun removeRecurrence(recurrenceId: UUID) = viewModelScope.launch {
            repository.deleteByRecurrenceId(recurrenceId)
        }

        override fun getAllByUserIdFlow(userId: UUID): Flow<List<Recurrence>> = viewModelScope.run {
            repository.getAllByUserIdFlow(userId)
        }

        override fun getByRecurrenceId(recurrenceId: UUID) = viewModelScope.run {
            repository.getByRecurrenceId(recurrenceId)
        }

        override fun getByRecurrenceIdFlow(recurrenceId: UUID): Flow<Recurrence> =
            viewModelScope.run {
                repository.getByRecurrenceIdFlow(recurrenceId)
            }
    }
}