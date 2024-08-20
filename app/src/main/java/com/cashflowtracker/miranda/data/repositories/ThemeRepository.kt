package com.cashflowtracker.miranda.data.repositories

import android.content.Context
import android.content.res.Configuration
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object ThemeRepository {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private val IS_DARK_THEME_KEY = booleanPreferencesKey("is_dark_theme")
    private val IS_FOLLOW_SYSTEM_KEY = booleanPreferencesKey("is_follow_system")

    /** Saves theme preference of app */
    suspend fun Context.saveThemePreference(isDarkTheme: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_DARK_THEME_KEY] = isDarkTheme
        }
    }

    /** Gets theme preference of app
     *  @return true if dark theme, false otherwise
     */
    suspend fun Context.getThemePreference(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[IS_DARK_THEME_KEY] ?: false // Defaults to light theme if no key is found
        }
    }

    /** Saves system preference of app: if true, follow system theme, if false use app custom choice */
    suspend fun Context.saveSystemPreference(isFollowSystem: Boolean) {
        dataStore.edit { preferences ->
            preferences[IS_FOLLOW_SYSTEM_KEY] = isFollowSystem
        }
    }

    /** Gets system preference of app
     *  @return true if follow system theme, false otherwise
     */
    suspend fun Context.getSystemPreference(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[IS_FOLLOW_SYSTEM_KEY] ?: true // Defaults to system theme if no key is found
        }
    }

    /** Gets current system theme preference */
    fun Context.getSystemDefaultTheme(): Boolean {
        return resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }
}