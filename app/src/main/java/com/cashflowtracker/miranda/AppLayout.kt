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

@Composable
fun AppLayout(
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Mostra la bottom bar solo se non siamo nelle schermate Settings, Profile, o TransactionAdd
    val showBottomBar = currentRoute != NavigationRoute.Settings.route &&
            currentRoute != NavigationRoute.Profile.route &&
            currentRoute != NavigationRoute.AddTransaction.route


    Scaffold(
        topBar = { AppBar(navController, isDarkTheme) },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(navController)
            }
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = NavigationRoute.Home.route
            ) {
                composable(NavigationRoute.Home.route) {
                    Home(navController)
                }
                composable(NavigationRoute.Transactions.route) {
                    Transactions(navController)
                }
                composable(NavigationRoute.Recurrents.route) {
                    Recurrents(navController)
                }
                composable(NavigationRoute.Stats.route) {
                    Stats(navController)
                }
                composable(NavigationRoute.Settings.route) {
                    Settings(
                        navController = navController,
                        isDarkTheme = isDarkTheme,  // Passa lo stato corrente del tema
                        onThemeChange = onThemeChange
                    )
                }
                composable(NavigationRoute.Profile.route) {
                    ProfileScreen(navController)
                }
                composable(NavigationRoute.AddTransaction.route) {
                    AddTransaction(navController)
                }
            }
        }
    }
}
