package com.cashflowtracker.miranda.ui.viewmodels

import android.content.Context
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cashflowtracker.miranda.data.repositories.ThemeRepository.getSystemPreferenceFlow
import com.cashflowtracker.miranda.data.repositories.ThemeRepository.getThemePreferenceFlow
import com.cashflowtracker.miranda.data.repositories.ThemeRepository.saveSystemPreference
import com.cashflowtracker.miranda.data.repositories.ThemeRepository.saveThemePreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ThemeViewModel(context: Context) :
    ViewModel() {
    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme = _isDarkTheme.asStateFlow()

    private val _followSystem = MutableStateFlow(true)
    val followSystem = _followSystem.asStateFlow()

    init {
        viewModelScope.launch {
            context.getThemePreferenceFlow().collect { isDark ->
                _isDarkTheme.value = isDark
            }
        }
        viewModelScope.launch {
            context.getSystemPreferenceFlow().collect { follow ->
                _followSystem.value = follow
            }
        }
    }

    /** Set Light/Dark Theme
     *  @param isDark true for Dark, false for Light
     */
    fun setThemePreference(isDark: Boolean, context: Context) = viewModelScope.launch {
        _isDarkTheme.value = isDark
        context.saveThemePreference(isDark)
    }

    fun getThemePreference(): Flow<Boolean> = viewModelScope.run {
        return isDarkTheme
    }

    /** Set Follow System Theme
     *  @param follow true for follow system, false for custom app theme
     */
    fun setSystemPreference(follow: Boolean, context: Context) = viewModelScope.launch {
        _followSystem.value = follow
        context.saveSystemPreference(follow)
    }

    fun getSystemPreference(): Flow<Boolean> = viewModelScope.run {
        return followSystem
    }
}