package com.cashflowtracker.miranda.data.repositories

import com.cashflowtracker.miranda.data.database.Account
import com.cashflowtracker.miranda.data.database.AccountsDao
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class AccountsRepository(private val accountsDao: AccountsDao) {
    suspend fun upsert(account: Account) = accountsDao.upsert(account)
    suspend fun delete(account: Account) = accountsDao.delete(account)
    fun getByTitle(title: String, userId: UUID) = accountsDao.getByTitle(title, userId)
    fun getByTitleOrNull(title: String, userId: UUID) = accountsDao.getByTitleOrNull(title, userId)
    fun getAllByUserId(userId: UUID) = accountsDao.getAllByUserId(userId)
    suspend fun setIsFavorite(title: String, userId: UUID, isFavorite: Boolean) =
        accountsDao.setIsFavorite(title, userId, isFavorite)

    fun getTotalBalance(userId: UUID): Flow<Double> = accountsDao.getTotalBalance(userId)
//    val accounts: Flow<List<Account>>? = accountsDao.getAllByUserId(userId)
}

