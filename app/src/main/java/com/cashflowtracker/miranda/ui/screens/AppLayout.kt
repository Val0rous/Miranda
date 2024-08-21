package com.cashflowtracker.miranda.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.ui.composables.ExpandableFAB
import com.cashflowtracker.miranda.ui.composables.ExtendedFAB
import com.cashflowtracker.miranda.ui.composables.HomeStatsTopAppBar
import com.cashflowtracker.miranda.ui.composables.Navbar
import com.cashflowtracker.miranda.ui.composables.RecurrentsTopAppBar
import com.cashflowtracker.miranda.ui.composables.TransactionsTopAppBar
import com.cashflowtracker.miranda.ui.theme.MirandaTheme
import com.cashflowtracker.miranda.utils.Routes

class AppLayout : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val startDestination = intent.getStringExtra("startDestination") ?: Routes.Home.route
        setContent {
            val isFabExpanded = remember { mutableStateOf(false) }
            val navController = rememberNavController()
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            val modifier = if (currentRoute.equals(Routes.Home.route) and isFabExpanded.value) {
                Modifier
                    .background(Color.Black.copy(alpha = 0.5f))
                    .fillMaxSize()
                    .clickable { isFabExpanded.value = false }
            } else {
                Modifier
                    .fillMaxSize()
            }

            MirandaTheme() {
                Scaffold(
                    modifier = modifier,
                    topBar = {
                        when (currentRoute) {
                            Routes.Home.route, Routes.Stats.route -> {
                                HomeStatsTopAppBar()
                            }

                            Routes.Transactions.route -> {
                                TransactionsTopAppBar()
                            }

                            Routes.Recurrents.route -> {
                                RecurrentsTopAppBar()
                            }
                        }
                    },
                    bottomBar = { Navbar(navController) },
                    floatingActionButton = {
                        when (currentRoute) {
                            Routes.Home.route -> {
                                ExpandableFAB(isFabExpanded)
                            }

                            Routes.Transactions.route -> {
                                ExtendedFAB(R.drawable.ic_assignment, "Add Transaction")
                            }

                            Routes.Recurrents.route -> {
                                ExtendedFAB(R.drawable.ic_schedule, "Add Recurrence")
                            }

                            else -> { /* No FAB */
                            }
                        }
                    },
                    floatingActionButtonPosition = FabPosition.End
                ) { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = startDestination,
                        modifier = Modifier.padding(paddingValues = paddingValues)
                    ) {
                        composable(Routes.Home.route) {
                            Home()
                        }
                        composable(Routes.Transactions.route) {
                            Transactions()
                        }
                        composable(Routes.Recurrents.route) {
                            Recurrents()
                        }
                        composable(Routes.Stats.route) {
                            Stats()
                        }
                    }
                }
            }
        }
    }
}
