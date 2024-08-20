package com.cashflowtracker.miranda

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Leaderboard
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Navigation(val route: String) {
    data object Home : Navigation("home")
    data object Transactions : Navigation("transactions")
    data object Recurrents : Navigation("recurrents")
    data object Stats : Navigation("stats")
    data object Settings : Navigation("settings")
    data object Profile : Navigation("profile")
    data object AddTransaction : Navigation("add_transaction")
    data object AddRecurrence : Navigation("add_recurrence")
    data object Login : Navigation("login")
    data object Signup : Navigation("signup")
}
