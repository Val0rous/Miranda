package com.cashflowtracker.miranda.ui.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cashflowtracker.miranda.data.database.User
import com.cashflowtracker.miranda.data.repositories.UserPreferencesRepository
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
    fun addUser(user: User): Job
    fun removeUser(user: User): Job
    suspend fun login(email: String, password: String): Boolean
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

    // TODO
    //val loginState = UserPreferencesRepository(context)

    val actions = object : UsersActions {
        override fun addUser(user: User) = viewModelScope.launch {
            repository.insert(user)
        }

        override fun removeUser(user: User) = viewModelScope.launch {
            repository.delete(user)
        }

        override suspend fun login(email: String, password: String): Boolean = viewModelScope.run {
            val user =
                async(Dispatchers.IO) { repository.getByEmail(email) }.await() // Only locally, change if client-server
            val saltedPassword = hashPassword(password, user!!.salt)
            return user.password == saltedPassword
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