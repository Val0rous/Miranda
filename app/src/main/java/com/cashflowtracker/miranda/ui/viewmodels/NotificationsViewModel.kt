package com.cashflowtracker.miranda.ui.viewmodels

import androidx.compose.material3.rememberDatePickerState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cashflowtracker.miranda.data.database.Notification
import com.cashflowtracker.miranda.data.database.RecurrenceWithNotification
import com.cashflowtracker.miranda.data.repositories.NotificationsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID

interface NotificationsActions {
    fun addNotification(notification: Notification): Job
    fun updateNotification(notification: Notification): Job
    fun removeNotification(notification: Notification): Job
    fun removeAllByRecurrenceId(recurrenceId: UUID): Job
    fun getAllByUserIdFlow(userId: UUID): Flow<List<Notification>>
    fun getAllByRecurrenceIdFlow(recurrenceId: UUID): Flow<List<Notification>>
}

class NotificationsViewModel(
    private val repository: NotificationsRepository
) : ViewModel() {
    val actions = object : NotificationsActions {
        override fun addNotification(notification: Notification) = viewModelScope.launch {
            repository.upsert(notification)
        }

        override fun updateNotification(notification: Notification) = addNotification(notification)

        override fun removeNotification(notification: Notification) = viewModelScope.launch {
            repository.delete(notification)
        }

        override fun removeAllByRecurrenceId(recurrenceId: UUID) =
            viewModelScope.launch(Dispatchers.IO) {
                repository.getAllByRecurrenceId(recurrenceId).forEach {
                    repository.delete(it)
                }
            }

        override fun getAllByUserIdFlow(userId: UUID): Flow<List<Notification>> =
            viewModelScope.run {
                repository.getAllByUserIdFlow(userId)
            }

        override fun getAllByRecurrenceIdFlow(recurrenceId: UUID): Flow<List<Notification>> =
            viewModelScope.run {
                repository.getAllByRecurrenceIdFlow(recurrenceId)
            }
    }
}