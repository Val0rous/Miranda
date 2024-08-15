package com.cashflowtracker.miranda

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class NavigationRoute(
    val route: String,
    val filledIcon: ImageVector,
    val outlinedIcon: ImageVector,
    val label: String
) {
    data object Home : NavigationRoute("home",Icons.Outlined.Home, Icons.Filled.Home , "Home")
    data object Transactions : NavigationRoute("transactions", Icons.Outlined.Assignment, Icons.Filled.Assignment, "Transactions")
    data object Recurrents : NavigationRoute("recurrents", Icons.Outlined.Schedule, Icons.Filled.Schedule, "Recurrents")
    data object Stats : NavigationRoute("stats", Icons.Outlined.Leaderboard, Icons.Filled.Leaderboard, "Stats")
    data object Settings : NavigationRoute("settings",Icons.Outlined.Settings,Icons.Filled.Settings,"Settings")
    data object Profile : NavigationRoute("profile", Icons.Default.Person, Icons.Default.Person, "Profile")


}
