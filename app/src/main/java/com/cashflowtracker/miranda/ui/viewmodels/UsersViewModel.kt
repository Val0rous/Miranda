package com.cashflowtracker.miranda.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cashflowtracker.miranda.data.database.User
import com.cashflowtracker.miranda.data.repositories.UsersRepository
import com.cashflowtracker.miranda.utils.generateSalt
import com.cashflowtracker.miranda.utils.hashPassword
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import kotlinx.coroutines.*

data class UsersState(val users: List<User>)

interface UsersActions {
    fun addUser(user: User): Job
    fun removeUser(user: User): Job
    suspend fun login(email: String, password: String): Boolean
    suspend fun signup(name: String, email: String, password: String): Boolean
    fun logout(user: User): Job
    fun getByEmail(email: String): User
    fun getByEmailOrNull(email: String): User?
    fun getUserIdByEmail(email: String): UUID
    fun getByUserId(userId: UUID): User
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

        override suspend fun login(email: String, password: String): Boolean = viewModelScope.run {
            val user =
                withContext(Dispatchers.IO) { repository.getByEmailOrNull(email) } // Only locally, change if client-server
            if (user == null) {
                return@run false
            }
            val saltedPassword = hashPassword(password, user.salt)
            return@run user.password == saltedPassword
        }

        override suspend fun signup(name: String, email: String, password: String): Boolean =
            viewModelScope.run {
                val existingUser = withContext(Dispatchers.IO) {
                    repository.getByEmailOrNull(email)
                }
                if (existingUser != null) {
                    return@run false
                } else {
                    val salt = generateSalt()
                    addUser(
                        User(
                            name = name,
                            email = email,
                            password = hashPassword(password, salt),
                            salt = salt,
                        )
                    )
                    return@run true
                }
            }

        override fun logout(user: User) = viewModelScope.launch {
            // TODO
        }

        override fun getByEmail(email: String): User {
            return repository.getByEmail(email)
        }

        override fun getByEmailOrNull(email: String): User? {
            return repository.getByEmailOrNull(email)
        }

        override fun getUserIdByEmail(email: String): UUID {
            return repository.getUserIdByEmail(email)
        }

        override fun getByUserId(userId: UUID): User {
            return repository.getByUserId(userId)
        }
    }
}