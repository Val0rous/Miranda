package com.cashflowtracker.miranda.data.repositories

import com.cashflowtracker.miranda.data.database.User
import com.cashflowtracker.miranda.data.database.UsersDao
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class UsersRepository(private val usersDao: UsersDao) {
    suspend fun insert(user: User) = usersDao.insert(user)
    suspend fun delete(user: User) = usersDao.delete(user)
    fun getUserIdByEmail(email: String) = usersDao.getUserIdByEmail(email)
    fun getByEmail(email: String) = usersDao.getByEmail(email)
    fun getByUserId(userId: UUID) = usersDao.getByUserId(userId)
    suspend fun updatePassword(userId: UUID, newPassword: String) =
        usersDao.updatePassword(userId, newPassword)

    suspend fun updateEmail(userId: UUID, newEmail: String) = usersDao.updateEmail(userId, newEmail)
    val users: Flow<List<User>>? = usersDao.listAll()
}