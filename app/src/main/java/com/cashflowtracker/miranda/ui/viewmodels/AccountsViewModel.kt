package com.cashflowtracker.miranda.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cashflowtracker.miranda.data.database.Account
import com.cashflowtracker.miranda.data.repositories.AccountsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

import java.util.UUID

data class AccountsState(val accounts: List<Account>)

interface AccountsActions {
    fun addAccount(account: Account): Job
    fun removeAccount(account: Account): Job
    fun getAllByEmail(email: String): List<Account>?
    fun getAllByUserId(userId: UUID): List<Account>?
}

class AccountsViewModel(private val repository: AccountsRepository) : ViewModel() {
    val state = repository.accounts!!.map { AccountsState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = AccountsState(emptyList())
        )

    val actions = object : AccountsActions {
        override fun addAccount(account: Account) = viewModelScope.launch {
            repository.upsert(account)
        }

        override fun removeAccount(account: Account) = viewModelScope.launch {
            repository.delete(account)
        }

        override fun getAllByEmail(email: String): List<Account>? {
            return null
        }

        override fun getAllByUserId(userId: UUID): List<Account>? {
            return null
        }
    }
}