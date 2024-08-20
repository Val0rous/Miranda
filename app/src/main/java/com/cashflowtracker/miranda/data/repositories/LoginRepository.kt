package com.cashflowtracker.miranda.data.repositories

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object LoginRepository {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "login")
    private val LOGGED_USER_EMAIL_KEY = stringPreferencesKey("logged_user_email")
    //private val LOGGED_USER_PROFILE_PICTURE_KEY = stringPreferencesKey("logged_user_profile_picture") // TODO

    suspend fun Context.saveLoggedUserEmail(email: String) {
        dataStore.edit { preferences ->
            preferences[LOGGED_USER_EMAIL_KEY] = email
        }
    }

    suspend fun Context.clearLoggedUserEmail() {
        dataStore.edit { preferences ->
            preferences.remove(LOGGED_USER_EMAIL_KEY)
        }
    }

    /** Gets logged user email from dataStore
     *  @return email if user is logged in, null otherwise
     */
    fun Context.getLoggedUserEmail(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[LOGGED_USER_EMAIL_KEY]
        }
    }
}