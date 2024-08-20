package com.cashflowtracker.miranda.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cashflowtracker.miranda.data.database.User
import com.cashflowtracker.miranda.data.repositories.UsersRepository
import com.cashflowtracker.miranda.utils.hashPassword
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import kotlinx.coroutines.*

data class UsersState(val users: List<User>)
data class CurrentUser(val currentUser: User?)

interface UsersActions {
    suspend fun addUser(user: User): Job
    suspend fun removeUser(user: User): Job
    fun login(email: String, password: String): Boolean
    fun logout(user: User): Job
    fun getByEmail(email: String): User?
    fun getByUserId(userId: UUID): User?
}

class UsersViewModel(private val repository: UsersRepository) : ViewModel() {
    val state = repository.users!!.map { UsersState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = UsersState(emptyList())
        )

    val actions = object : UsersActions {
        override suspend fun addUser(user: User) = viewModelScope.launch {
            repository.insert(user)
        }

        override suspend fun removeUser(user: User) = viewModelScope.launch {
            repository.delete(user)
        }

        override fun login(email: String, password: String): Boolean {
            val user = repository.getByEmail(email)
            val saltedPassword = hashPassword(password, user!!.salt)
            if (user.password == saltedPassword) {
                return true
            }
            return false
        }

        override fun logout(user: User) = viewModelScope.launch {
            // TODO
        }

        override fun getByEmail(email: String): User? {
            return repository.getByEmail(email)
        }

        override fun getByUserId(userId: UUID): User? {
            return repository.getByUserId(userId)
        }
    }
}