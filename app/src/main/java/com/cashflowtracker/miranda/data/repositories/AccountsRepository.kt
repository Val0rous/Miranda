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

    fun getByAccountId(accountId: UUID) =
        accountsDao.getByAccountId(accountId)

    fun getByAccountIdOrNull(accountId: UUID) =
        accountsDao.getByAccountIdOrNull(accountId)

    fun getByAccountIdFlow(accountId: UUID) =
        accountsDao.getByAccountIdFlow(accountId)

    fun getAllByUserId(userId: UUID) = accountsDao.getAllByUserId(userId)
    fun getByTitleOrNull(title: String, userId: UUID) = accountsDao.getByTitleOrNull(title, userId)
    suspend fun setIsFavorite(accountId: UUID, userId: UUID, isFavorite: Boolean) =
        accountsDao.setIsFavorite(accountId, userId, isFavorite)

    fun getTotalBalance(userId: UUID): Flow<Double> = accountsDao.getTotalBalance(userId)
    suspend fun updateBalance(accountId: UUID, amount: Double) =
        accountsDao.updateBalance(accountId, amount)

    fun getTypeByTitle(title: String, userId: UUID) = accountsDao.getTypeByTitle(title, userId)
    fun getTypeByAccountId(accountId: UUID) = accountsDao.getTypeByAccountId(accountId)
//    val accounts: Flow<List<Account>>? = accountsDao.getAllByUserId(userId)
}

