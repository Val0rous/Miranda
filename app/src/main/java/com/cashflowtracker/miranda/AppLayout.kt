package com.cashflowtracker.miranda

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cashflowtracker.miranda.ui.screens.AddRecurrence
import com.cashflowtracker.miranda.ui.screens.AddTransaction
import com.cashflowtracker.miranda.ui.screens.Home
import com.cashflowtracker.miranda.ui.screens.Profile
import com.cashflowtracker.miranda.ui.screens.Recurrents
import com.cashflowtracker.miranda.ui.screens.Settings
import com.cashflowtracker.miranda.ui.screens.Stats
import com.cashflowtracker.miranda.ui.screens.Transactions

@Composable
fun AppLayout(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Hide bottom bar in Settings, Profile, AddTransaction and AddRecurrence
    val showBottomBar = currentRoute != Navigation.Settings.route
            && currentRoute != Navigation.Profile.route
            && currentRoute != Navigation.AddTransaction.route
            && currentRoute != Navigation.AddRecurrence.route
    //&& currentRoute != Navigation.Signup.route
    //&& currentRoute != NavigationRoute.Login.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { AppBar(navController, isDarkTheme) },
        bottomBar = {
            if (showBottomBar) {
                Navbar(navController)
            }
        }
    ) { paddingValues ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues)
//        ) {
        NavHost(
            navController = navController,
            startDestination = Navigation.Home.route,
            modifier = Modifier.padding(paddingValues = paddingValues)
        ) {
            composable(Navigation.Home.route) {
                Home(navController)
            }
            composable(Navigation.Transactions.route) {
                Transactions(navController)
            }
            composable(Navigation.Recurrents.route) {
                Recurrents(navController)
            }
            composable(Navigation.Stats.route) {
                Stats(navController)
            }
            composable(Navigation.Settings.route) {
                Settings(
                    navController = navController,
                    isDarkTheme = isDarkTheme,  // Passa lo stato corrente del tema
                    onThemeChange = onThemeChange
                )
            }
            composable(Navigation.Profile.route) {
                Profile(navController)
            }
            composable(Navigation.AddTransaction.route) {
                AddTransaction(navController)
            }
            composable(Navigation.AddRecurrence.route) {
                AddRecurrence(navController)
            }
//                composable(NavigationRoute.Signup.route) {
//                    Signup(navController)
//                }
        }
//        }
    }
}
