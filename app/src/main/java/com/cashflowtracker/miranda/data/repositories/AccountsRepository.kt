package com.cashflowtracker.miranda.data.repositories

import com.cashflowtracker.miranda.data.database.Account
import com.cashflowtracker.miranda.data.database.AccountsDao
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class AccountsRepository(private val accountsDao: AccountsDao) {
    suspend fun upsert(account: Account) = accountsDao.upsert(account)
    suspend fun delete(account: Account) = accountsDao.delete(account)
    suspend fun deleteByAccountId(accountId: UUID) =
        accountsDao.deleteByAccountId(accountId)

    fun getByAccountId(accountId: UUID, userId: UUID) =
        accountsDao.getByAccountId(accountId, userId)

    fun getByAccountIdOrNull(accountId: UUID, userId: UUID) =
        accountsDao.getByAccountIdOrNull(accountId, userId)

    fun getByAccountIdFlow(accountId: UUID, userId: UUID) =
        accountsDao.getByAccountIdFlow(accountId, userId)

    fun getAllByUserId(userId: UUID) = accountsDao.getAllByUserId(userId)
    fun getByTitleOrNull(title: String, userId: UUID) = accountsDao.getByTitleOrNull(title, userId)
    suspend fun setIsFavorite(accountId: UUID, userId: UUID, isFavorite: Boolean) =
        accountsDao.setIsFavorite(accountId, userId, isFavorite)

    fun getTotalBalance(userId: UUID): Flow<Double> = accountsDao.getTotalBalance(userId)
    suspend fun updateBalance(accountId: UUID, amount: Double) =
        accountsDao.updateBalance(accountId, amount)

    fun getTypeByTitle(title: String, userId: UUID) = accountsDao.getTypeByTitle(title, userId)
//    val accounts: Flow<List<Account>>? = accountsDao.getAllByUserId(userId)
}

