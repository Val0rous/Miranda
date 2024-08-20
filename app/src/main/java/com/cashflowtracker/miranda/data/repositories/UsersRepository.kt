package com.cashflowtracker.miranda.data.repositories

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.cashflowtracker.miranda.data.database.User
import com.cashflowtracker.miranda.data.database.UsersDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

class UserPreferencesRepository(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")
        val LOGGED_IN_EMAIL_KEY = stringPreferencesKey("logged_in_email")
    }

    // Store logged-in email
    suspend fun storeLoggedInEmail(email: String) {
        context.dataStore.edit { preferences ->
            preferences[LOGGED_IN_EMAIL_KEY] = email
        }
    }

    // Retrieve logged-in email
    val getLoggedInEmail: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[LOGGED_IN_EMAIL_KEY]
    }
}