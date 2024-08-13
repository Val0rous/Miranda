package com.cashflowtracker.miranda

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppLayout() {
    val navController = rememberNavController()
    Scaffold(
        topBar = { AppBar() },
        floatingActionButton = {
            FloatingActionButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.Add, "Add item")
            }
        },
        bottomBar = { BottomNavigationBar(navController) }
    ) { contentPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding)) {
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
            }
        }
    }
}