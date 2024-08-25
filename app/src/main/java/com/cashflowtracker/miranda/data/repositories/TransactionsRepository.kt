package com.cashflowtracker.miranda.data.repositories

import com.cashflowtracker.miranda.data.database.Transaction
import com.cashflowtracker.miranda.data.database.TransactionsDao
import java.util.UUID

class TransactionsRepository(private val transactionsDao: TransactionsDao) {
    suspend fun upsert(transaction: Transaction) = transactionsDao.upsert(transaction)
    suspend fun delete(transaction: Transaction) = transactionsDao.delete(transaction)
    suspend fun deleteByTransactionId(transactionId: UUID) =
        transactionsDao.deleteByTransactionId(transactionId)

    fun getAllByUserId(userId: UUID) = transactionsDao.getAllByUserId(userId)
    fun getByTransactionId(transactionId: UUID) = transactionsDao.getByTransactionId(transactionId)
    fun getByTransactionIdFlow(transactionId: UUID) =
        transactionsDao.getByTransactionIdFlow(transactionId)

    suspend fun updateAllByTitle(oldTitle: String, newTitle: String) =
        transactionsDao.updateAllByTitle(oldTitle, newTitle)
}