package com.cashflowtracker.miranda.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cashflowtracker.miranda.data.database.Account
import com.cashflowtracker.miranda.data.repositories.AccountsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

import java.util.UUID

data class AccountsState(val accounts: List<Account>)

interface AccountsActions {
    fun addAccount(account: Account): Job
    fun updateAccount(account: Account): Job
    fun removeAccount(account: Account): Job
    fun removeAccount(accountId: UUID): Job
    fun getAllByUserId(userId: UUID): Flow<List<Account>>
    fun getByAccountId(accountId: UUID): Account
    fun getByAccountIdFlow(accountId: UUID): Flow<Account>
    fun getByAccountIdOrNull(accountId: UUID): Account?
    fun getByTitleOrNull(title: String, userId: UUID): Account?
    fun toggleIsFavorite(accountId: UUID, userId: UUID, isFavorite: Boolean): Job
    fun getTotalBalance(userId: UUID): Flow<Double>
    fun updateBalance(accountId: UUID, amount: Double): Job
    fun getTypeByTitle(title: String, userId: UUID): Flow<String>
    fun getTypeByAccountId(accountId: UUID): Flow<String>
}

class AccountsViewModel(private val repository: AccountsRepository) : ViewModel() {
//    val state = repository.getAllByUserId(userId).map { AccountsState(it) }
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(),
//            initialValue = AccountsState(emptyList())
//        )

    val actions = object : AccountsActions {
        override fun addAccount(account: Account) = viewModelScope.launch {
            repository.upsert(account)
        }

        override fun updateAccount(account: Account) = addAccount(account)

        override fun removeAccount(account: Account) = viewModelScope.launch {
            repository.delete(account)
        }

        override fun removeAccount(accountId: UUID) = viewModelScope.launch {
            repository.deleteByAccountId(accountId)
        }

        override fun getByAccountId(accountId: UUID) = viewModelScope.run {
            repository.getByAccountId(accountId)
        }

        override fun getByAccountIdFlow(accountId: UUID): Flow<Account> =
            viewModelScope.run {
                repository.getByAccountIdFlow(accountId)
            }

        override fun getByAccountIdOrNull(accountId: UUID) = viewModelScope.run {
            repository.getByAccountIdOrNull(accountId)
        }

        override fun getByTitleOrNull(title: String, userId: UUID) = viewModelScope.run {
            repository.getByTitleOrNull(title, userId)
        }

        override fun getAllByUserId(userId: UUID): Flow<List<Account>> = viewModelScope.run {
            repository.getAllByUserId(userId)
        }

        override fun toggleIsFavorite(accountId: UUID, userId: UUID, isFavorite: Boolean) =
            viewModelScope.launch {
                repository.setIsFavorite(accountId, userId, isFavorite)
            }

        override fun getTotalBalance(userId: UUID): Flow<Double> = viewModelScope.run {
            repository.getTotalBalance(userId)
        }

        override fun updateBalance(accountId: UUID, amount: Double) = viewModelScope.launch {
            repository.updateBalance(accountId, amount)
        }

        override fun getTypeByTitle(title: String, userId: UUID): Flow<String> =
            viewModelScope.run {
                repository.getTypeByTitle(title, userId)
            }

        override fun getTypeByAccountId(accountId: UUID): Flow<String> = viewModelScope.run {
            viewModelScope.run {
                repository.getTypeByAccountId(accountId)
            }
        }
    }
}