package com.cashflowtracker.miranda

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(navController: NavHostController, isDarkTheme: Boolean) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    TopAppBar(
        title = {
            Text(
                text = when (currentRoute) {
                    NavigationRoute.Settings.route -> "Settings"
                    NavigationRoute.Transactions.route -> "Transactions"
                    NavigationRoute.Recurrents.route -> "Recurrents"
                    NavigationRoute.Stats.route -> "Stats"
                    NavigationRoute.Profile.route -> "Your Profile"
                    else -> ""
                },
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        navigationIcon = {
            if (currentRoute == NavigationRoute.Settings.route || currentRoute == NavigationRoute.Profile.route) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            }
        },
        actions = {
            if (currentRoute != NavigationRoute.Settings.route && currentRoute != NavigationRoute.Profile.route) {
                when (currentRoute) {
                    NavigationRoute.Transactions.route -> {
                        IconButton(onClick = { /* 1st Action */ }) {
                            Icon(Icons.Filled.SwapVert, contentDescription = "Sort")
                        }
                        IconButton(onClick = { /* 2nd Action */ }) {
                            Icon(Icons.Filled.FilterList, contentDescription = "Filter")
                        }
                        IconButton(onClick = { /* 3rd Action */ }) {
                            Icon(Icons.Filled.Map, contentDescription = "Map View")
                        }
                    }

                    NavigationRoute.Recurrents.route -> {
                        IconButton(onClick = { /* 1st Action */ }) {
                            Icon(Icons.Filled.SwapVert, contentDescription = "Sort")
                        }
                        IconButton(onClick = { /* 2nd Action */ }) {
                            Icon(Icons.Filled.FilterList, contentDescription = "Filter")
                        }
                    }

                    NavigationRoute.Stats.route -> {
                        // Eventually add specific icons for Stats screen
                    }
                }

                IconButton(onClick = { navController.navigate(NavigationRoute.Settings.route) }) {
                    Icon(Icons.Rounded.Settings, contentDescription = "Settings")
                }
                IconButton(onClick = { navController.navigate(NavigationRoute.Profile.route) }) {
                    Icon(Icons.Filled.AccountCircle, contentDescription = "Profile")
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = if (isDarkTheme) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primaryContainer
        )
    )
}
