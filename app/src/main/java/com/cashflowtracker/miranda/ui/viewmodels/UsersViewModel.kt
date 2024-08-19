package com.cashflowtracker.miranda.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cashflowtracker.miranda.data.database.User
import com.cashflowtracker.miranda.data.repositories.UsersRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class UsersState(val users: List<User>)
data class CurrentUser(val currentUser: User?)

interface UsersActions {
    fun addUser(user: User): Job
    fun removeUser(user: User): Job
    fun login(user: User): Job
    fun logout(user: User): Job
}

class UsersViewModel(private val repository: UsersRepository) : ViewModel() {
    val state = repository.users.map { UsersState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = UsersState(emptyList())
        )

    val actions = object : UsersActions {
        override fun addUser(user: User) = viewModelScope.launch {
            repository.insert(user)
        }

        override fun removeUser(user: User) = viewModelScope.launch {
            repository.delete(user)
        }

        override fun login(user: User) = viewModelScope.launch {
            // TODO
        }

        override fun logout(user: User) = viewModelScope.launch {
            // TODO
        }
    }
}