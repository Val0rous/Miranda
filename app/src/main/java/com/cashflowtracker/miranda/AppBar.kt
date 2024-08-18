package com.cashflowtracker.miranda

import android.content.res.Resources
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(navController: NavHostController, isDarkTheme: Boolean) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    if (currentRoute == NavigationRoute.AddTransaction.route
        || currentRoute == NavigationRoute.AddRecurrence.route
    ) {
        return
    }

    TopAppBar(
        title = {
            Text(
                text = when (currentRoute) {
                    NavigationRoute.Settings.route -> "Settings"
                    NavigationRoute.Transactions.route -> ""
                    NavigationRoute.Recurrents.route -> ""
                    NavigationRoute.Stats.route -> ""
                    NavigationRoute.Profile.route -> "Your Profile"
                    else -> ""
                },
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.onBackground
            )
        },
        navigationIcon = {
            if (currentRoute == NavigationRoute.Settings.route || currentRoute == NavigationRoute.Profile.route) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_back),
                        contentDescription = "Back"
                    )
                }
            }
        },
        actions = {
            if (currentRoute != NavigationRoute.Settings.route && currentRoute != NavigationRoute.Profile.route) {
                when (currentRoute) {
                    NavigationRoute.Transactions.route -> {
                        IconButton(onClick = { /* 1st Action */ }) {
                            Icon(
                                ImageVector.vectorResource(R.drawable.ic_swap_vert),
                                contentDescription = "Sort"
                            )
                        }
                        IconButton(onClick = { /* 2nd Action */ }) {
                            Icon(
                                ImageVector.vectorResource(R.drawable.ic_filter_list),
                                contentDescription = "Filter"
                            )
                        }
                        IconButton(onClick = { /* 3rd Action */ }) {
                            Icon(
                                ImageVector.vectorResource(R.drawable.ic_map),
                                contentDescription = "Map View"
                            )
                        }
                    }

                    NavigationRoute.Recurrents.route -> {
                        IconButton(onClick = { /* 1st Action */ }) {
                            Icon(
                                ImageVector.vectorResource(R.drawable.ic_swap_vert),
                                contentDescription = "Sort"
                            )
                        }
                        IconButton(onClick = { /* 2nd Action */ }) {
                            Icon(
                                ImageVector.vectorResource(R.drawable.ic_filter_list),
                                contentDescription = "Filter"
                            )
                        }
                    }

                    NavigationRoute.Stats.route -> {
                        // Eventually add specific icons for Stats screen
                    }
                }

                IconButton(onClick = { navController.navigate(NavigationRoute.Settings.route) }) {
                    Icon(
                        ImageVector.vectorResource(R.drawable.ic_settings),
                        contentDescription = "Settings"
                    )
                }
                IconButton(
                    onClick = { navController.navigate(NavigationRoute.Profile.route) },
                    colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        ImageVector.vectorResource(R.drawable.ic_account_circle_filled),
                        contentDescription = "Profile",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = if (isDarkTheme) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primaryContainer
        ),
    )
}
