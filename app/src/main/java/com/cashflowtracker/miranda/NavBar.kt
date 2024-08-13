package com.cashflowtracker.miranda

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Assignment
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Leaderboard
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

sealed class NavigationRoute(val route: String, val icon: ImageVector, val label: String) {
    data object Home : NavigationRoute("home", Icons.Outlined.Home, "Home")
    data object Transactions : NavigationRoute("transactions", Icons.Outlined.Assignment, "Transactions")
    data object Recurrents : NavigationRoute("recurrents", Icons.Outlined.Schedule, "Recurrents")
    data object Stats : NavigationRoute("stats", Icons.Outlined.Leaderboard, "Stats")
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    BottomNavigation {

    }
}