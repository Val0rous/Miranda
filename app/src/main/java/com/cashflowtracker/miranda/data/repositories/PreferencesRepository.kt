package com.cashflowtracker.miranda.data.repositories

import android.content.Context

object PreferencesRepository {
    private const val PREFS_FILE_NAME = "settings_prefs"
    private const val BALANCE_VISIBILITY_KEY = "balance_visibility"

    fun Context.setBalanceVisibility(isVisible: Boolean) {
        val sharedPrefs = getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
        sharedPrefs.edit().putBoolean(BALANCE_VISIBILITY_KEY, isVisible).apply()
    }

    fun Context.getBalanceVisibility(): Boolean {
        val sharedPrefs = getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
        return sharedPrefs.getBoolean(BALANCE_VISIBILITY_KEY, true)
    }

}