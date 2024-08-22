package com.cashflowtracker.miranda.data.repositories

import com.cashflowtracker.miranda.data.database.Account
import com.cashflowtracker.miranda.data.database.AccountsDao
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class AccountsRepository(private val accountsDao: AccountsDao, private val userId: UUID) {
    suspend fun upsert(account: Account) = accountsDao.upsert(account)
    suspend fun delete(account: Account) = accountsDao.delete(account)
    fun getByTitle(title: String, userId: UUID) = accountsDao.getByTitle(title, userId)
    val accounts: Flow<List<Account>>? = accountsDao.getAllByUserId(userId)
}

