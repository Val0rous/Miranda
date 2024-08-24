package com.cashflowtracker.miranda.data.repositories

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

object ThemeRepository {
    private const val IS_DARK_THEME_KEY = "is_dark_theme"
    private const val IS_FOLLOW_SYSTEM_KEY = "is_follow_system"

    /** Saves theme preference of app
     *  @param isDarkTheme true for dark theme, false for light theme
     */
    fun Context.saveThemePreference(isDarkTheme: Boolean) {
        val sharedPrefs = getDefaultSharedPreferences(this)
        sharedPrefs.edit().putBoolean(IS_DARK_THEME_KEY, isDarkTheme).apply()
    }

    /** Gets theme preference of app
     *  @return true if dark theme, false if light theme
     */
    fun Context.getThemePreference(): Boolean {
        val sharedPrefs = getDefaultSharedPreferences(this)
        return sharedPrefs.getBoolean(IS_DARK_THEME_KEY, false) // Default to light theme
    }

    fun Context.getThemePreferenceFlow(): Flow<Boolean> = callbackFlow {
        val sharedPrefs = getDefaultSharedPreferences(this@getThemePreferenceFlow)
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == IS_DARK_THEME_KEY) {
                trySend(sharedPrefs.getBoolean(IS_DARK_THEME_KEY, false))
            }
        }
        sharedPrefs.registerOnSharedPreferenceChangeListener(listener)
        trySend(sharedPrefs.getBoolean(IS_DARK_THEME_KEY, false))
        awaitClose {
            sharedPrefs.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }

    /** Saves system preference of app
     * @param isFollowSystem true to follow system theme, false to use app custom choice */
    fun Context.saveSystemPreference(isFollowSystem: Boolean) {
        val sharedPrefs = getDefaultSharedPreferences(this)
        sharedPrefs.edit().putBoolean(IS_FOLLOW_SYSTEM_KEY, isFollowSystem).apply()
    }

    /** Gets system preference of app
     *  @return true if following system theme, false if using custom app theme
     */
    fun Context.getSystemPreference(): Boolean {
        val sharedPrefs = getDefaultSharedPreferences(this)
        return sharedPrefs.getBoolean(IS_FOLLOW_SYSTEM_KEY, true)   // Default to system theme
    }

    fun Context.getSystemPreferenceFlow(): Flow<Boolean> = callbackFlow {
        val sharedPrefs = getDefaultSharedPreferences(this@getSystemPreferenceFlow)
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            if (key == IS_FOLLOW_SYSTEM_KEY) {
                trySend(sharedPrefs.getBoolean(IS_FOLLOW_SYSTEM_KEY, true))
            }
        }
        sharedPrefs.registerOnSharedPreferenceChangeListener(listener)
        trySend(sharedPrefs.getBoolean(IS_FOLLOW_SYSTEM_KEY, true))
        awaitClose {
            sharedPrefs.unregisterOnSharedPreferenceChangeListener(listener)
        }
    }

    /** Gets current system theme preference
     *  @return true if system theme is dark, false if system theme is light
     */
    fun Context.getSystemDefaultTheme(): Boolean {
        return resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }


//    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
//    private val IS_DARK_THEME_KEY = booleanPreferencesKey("is_dark_theme")
//    private val IS_FOLLOW_SYSTEM_KEY = booleanPreferencesKey("is_follow_system")
//
//    /** Saves theme preference of app */
//    suspend fun Context.saveThemePreference(isDarkTheme: Boolean) {
//        dataStore.edit { preferences ->
//            preferences[IS_DARK_THEME_KEY] = isDarkTheme
//        }
//    }
//
//    /** Gets theme preference of app
//     *  @return true if dark theme, false otherwise
//     */
//    fun Context.getThemePreference(): Flow<Boolean> {
//        return dataStore.data.map { preferences ->
//            preferences[IS_DARK_THEME_KEY] ?: false // Defaults to light theme if no key is found
//        }
//    }
//
//    /** Saves system preference of app: if true, follow system theme, if false use app custom choice */
//    suspend fun Context.saveSystemPreference(isFollowSystem: Boolean) {
//        dataStore.edit { preferences ->
//            preferences[IS_FOLLOW_SYSTEM_KEY] = isFollowSystem
//        }
//    }
//
//    /** Gets system preference of app
//     *  @return true if follow system theme, false otherwise
//     */
//    fun Context.getSystemPreference(): Flow<Boolean> {
//        return dataStore.data.map { preferences ->
//            preferences[IS_FOLLOW_SYSTEM_KEY] ?: true // Defaults to system theme if no key is found
//        }

//    }
}