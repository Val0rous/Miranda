package com.cashflowtracker.miranda.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cashflowtracker.miranda.data.database.Transaction
import com.cashflowtracker.miranda.data.repositories.TransactionsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID

interface TransactionsActions {
    fun addTransaction(transaction: Transaction): Job
    fun updateTransaction(transaction: Transaction): Job
    fun removeTransaction(transaction: Transaction): Job
    fun removeTransaction(transactionId: UUID): Job
    fun getAllByUserIdFlow(userId: UUID): Flow<List<Transaction>>
    fun getAllWithLocationByUserIdFlow(userId: UUID): Flow<List<Transaction>>
    fun getByTransactionId(transactionId: UUID): Transaction
    fun getByTransactionIdFlow(transactionId: UUID): Flow<Transaction>
}

class TransactionsViewModel(private val repository: TransactionsRepository) : ViewModel() {
    val actions = object : TransactionsActions {
        override fun addTransaction(transaction: Transaction) = viewModelScope.launch {
            repository.upsert(transaction)
        }

        override fun updateTransaction(transaction: Transaction) = addTransaction(transaction)

        override fun removeTransaction(transaction: Transaction) = viewModelScope.launch {
            repository.delete(transaction)
        }

        override fun removeTransaction(transactionId: UUID) = viewModelScope.launch {
            repository.deleteByTransactionId(transactionId)
        }

        override fun getAllByUserIdFlow(userId: UUID): Flow<List<Transaction>> =
            viewModelScope.run {
                repository.getAllByUserIdFlow(userId)
            }

        override fun getAllWithLocationByUserIdFlow(userId: UUID): Flow<List<Transaction>> =
            viewModelScope.run {
                repository.getAllWithLocationByUserIdFlow(userId)
            }

        override fun getByTransactionId(transactionId: UUID) = viewModelScope.run {
            repository.getByTransactionId(transactionId)
        }

        override fun getByTransactionIdFlow(transactionId: UUID): Flow<Transaction> =
            viewModelScope.run {
                repository.getByTransactionIdFlow(transactionId)
            }

    }
}