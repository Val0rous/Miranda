package com.cashflowtracker.miranda

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Leaderboard
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource

sealed class NavigationRoute(
    val route: String,
    val filledIcon: ImageVector,
    val outlinedIcon: ImageVector,
    val label: String
) {
    data object Home : NavigationRoute("home", Icons.Outlined.Home, Icons.Filled.Home, "Home")
    data object Transactions : NavigationRoute(
        "transactions",
        Icons.AutoMirrored.Outlined.Assignment,
        Icons.AutoMirrored.Filled.Assignment,
        "Transactions"
    )

    data object Recurrents :
        NavigationRoute("recurrents", Icons.Outlined.Schedule, Icons.Filled.Schedule, "Recurrents")

    data object Stats :
        NavigationRoute("stats", Icons.Outlined.Leaderboard, Icons.Filled.Leaderboard, "Stats")

    data object Settings :
        NavigationRoute("settings", Icons.Outlined.Settings, Icons.Rounded.Settings, "Settings")

    data object Profile :
        NavigationRoute("profile", Icons.Default.Person, Icons.Default.Person, "Profile")

    data object AddTransaction :
        NavigationRoute("add_transaction", Icons.Default.Add, Icons.Default.Add, "Add Transaction")

    data object AddRecurrence :
        NavigationRoute("add_recurrence", Icons.Default.Add, Icons.Default.Add, "Add Recurrence")

    data object Signup :
        NavigationRoute(
            "signup", Icons.AutoMirrored.Filled.Login,
            Icons.AutoMirrored.Filled.Login, "Signup"
        )
}


