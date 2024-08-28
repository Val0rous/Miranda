package com.cashflowtracker.miranda.data.repositories

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object PreferencesRepository {
    // SharedPreferences for simple and non-sensitive data
    private const val PREFS_FILE_NAME = "settings_prefs"
    private const val BALANCE_VISIBILITY_KEY = "balance_visibility"

    // DataStore for sensitive or complex data
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("user_preferences")
    private val PROFILE_PICTURE_PATH_KEY = stringPreferencesKey("profile_picture_path")

    fun Context.setBalanceVisibility(isVisible: Boolean) {
        val sharedPrefs = getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
        sharedPrefs.edit().putBoolean(BALANCE_VISIBILITY_KEY, isVisible).apply()
    }

    fun Context.getBalanceVisibility(): Boolean {
        val sharedPrefs = getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
        return sharedPrefs.getBoolean(BALANCE_VISIBILITY_KEY, true)
    }

    suspend fun Context.saveProfilePicturePath(path: String) {
        dataStore.edit { preferences ->
            preferences[PROFILE_PICTURE_PATH_KEY] = path
        }
    }

    fun Context.getProfilePicturePathFlow(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[PROFILE_PICTURE_PATH_KEY]
        }
    }
}