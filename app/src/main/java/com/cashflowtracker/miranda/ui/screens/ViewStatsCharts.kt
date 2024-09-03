package com.cashflowtracker.miranda.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.ui.composables.ChartTabs
import com.cashflowtracker.miranda.ui.theme.MirandaTheme
import com.cashflowtracker.miranda.utils.Routes

class ViewStatsCharts : ComponentActivity() {
    private lateinit var navController: NavHostController

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val initialStartDestination =
            intent.getStringExtra("startDestination") ?: Routes.OverallStats.route
        setContent {
            navController = rememberNavController()
            var startDestination by remember { mutableStateOf(initialStartDestination) }
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            MirandaTheme {
                Scaffold(
                    modifier = Modifier,
                    topBar = {
                        Column(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
                            TopAppBar(
                                title = {
                                    Text(
                                        text = "Cashflow Chart",
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                },
                                navigationIcon = {
                                    IconButton(onClick = { finish() }) {
                                        Icon(
                                            imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_back),
                                            contentDescription = "Back",
                                            tint = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                },
                                colors = TopAppBarDefaults.mediumTopAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.surface
                                )
                            )
                            ChartTabs(navController, startDestination)
                        }
                    },
                ) { paddingValues ->
                    LaunchedEffect(Unit) {
                        savedInstanceState?.getString("NAVIGATION_STATE")?.let { savedRoute ->
                            startDestination = savedRoute
                        }
                    }
                    NavHost(
                        navController = navController,
                        startDestination = startDestination,
                        modifier = Modifier.padding(paddingValues = paddingValues)
                    ) {
                        composable(Routes.OverallStats.route) {
                            OverallChart()
                        }
                        composable(Routes.YearlyStats.route) {
                            YearlyChart()
                        }
                        composable(Routes.QuarterlyStats.route) {
                            QuarterlyChart()
                        }
                        composable(Routes.MonthlyStats.route) {
                            MonthlyChart()
                        }
                    }
                }
            }
        }
    }
}