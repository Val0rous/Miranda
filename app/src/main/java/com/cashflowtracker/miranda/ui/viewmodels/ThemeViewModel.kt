package com.cashflowtracker.miranda.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ThemeViewModel : ViewModel() {
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme = _isDarkTheme.asStateFlow()

    private val _followSystem = MutableStateFlow(true)
    val followSystem = _followSystem.asStateFlow()

    /** Set Light/Dark Theme
     *  @param isDark true for Dark, false for Light
     */
    fun setThemePreference(isDark: Boolean) {
        _isDarkTheme.value = isDark
    }

    /** Set Follow System Theme
     *  @param follow true for follow system, false for custom app theme
     */
    fun setSystemPreference(follow: Boolean) {
        _followSystem.value = follow
    }
}