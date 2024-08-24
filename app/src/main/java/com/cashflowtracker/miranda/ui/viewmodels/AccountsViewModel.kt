package com.cashflowtracker.miranda.ui.viewmodels

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cashflowtracker.miranda.data.database.Account
import com.cashflowtracker.miranda.data.repositories.AccountsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

import java.util.UUID

data class AccountsState(val accounts: List<Account>)

interface AccountsActions {
    fun addAccount(account: Account): Job
    fun updateAccount(account: Account): Job
    fun removeAccount(account: Account): Job
    fun getAllByUserId(userId: UUID): Flow<List<Account>>
    fun getByTitle(title: String, userId: UUID): Account
    fun getByTitleOrNull(title: String, userId: UUID): Account?
    fun toggleIsFavorite(title: String, userId: UUID, isFavorite: Boolean): Job
    fun getTotalBalance(userId: UUID): Flow<Double>
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

        override fun getByTitle(title: String, userId: UUID) = viewModelScope.run {
            repository.getByTitle(title, userId)
        }

        override fun getByTitleOrNull(title: String, userId: UUID) = viewModelScope.run {
            repository.getByTitleOrNull(title, userId)
        }

        override fun getAllByUserId(userId: UUID): Flow<List<Account>> = viewModelScope.run {
            repository.getAllByUserId(userId)
        }

        override fun toggleIsFavorite(title: String, userId: UUID, isFavorite: Boolean) =
            viewModelScope.launch {
                repository.setIsFavorite(title, userId, isFavorite)
            }

        override fun getTotalBalance(userId: UUID): Flow<Double> = viewModelScope.run {
            repository.getTotalBalance(userId)
        }
    }
}