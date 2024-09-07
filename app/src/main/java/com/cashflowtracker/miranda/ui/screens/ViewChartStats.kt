package com.cashflowtracker.miranda.ui.screens

import android.app.Activity
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.vectorResource
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.data.repositories.LoginRepository.getCurrentUserId
import com.cashflowtracker.miranda.ui.composables.ChartTabs
import com.cashflowtracker.miranda.ui.composables.MonthlyChart
import com.cashflowtracker.miranda.ui.composables.OverallChart
import com.cashflowtracker.miranda.ui.composables.QuarterlyChart
import com.cashflowtracker.miranda.ui.composables.YearlyChart
import com.cashflowtracker.miranda.ui.theme.MirandaTheme
import com.cashflowtracker.miranda.ui.viewmodels.TransactionsViewModel
import com.cashflowtracker.miranda.utils.Routes
import org.koin.androidx.compose.koinViewModel

class ViewChartStats : ComponentActivity() {
    private lateinit var navController: NavHostController

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val initialStartDestination =
            intent.getStringExtra("startDestination") ?: Routes.OverallStats.route
        setContent {
            navController = rememberNavController()
            val coroutineScope = rememberCoroutineScope()
            var startDestination by remember { mutableStateOf(initialStartDestination) }
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            when (currentRoute) {
                Routes.YearlyStats.route,
                Routes.QuarterlyStats.route,
                Routes.MonthlyStats.route -> {
                    WindowCompat.setDecorFitsSystemWindows(window, false)
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                    window.navigationBarColor =
                        MaterialTheme.colorScheme.surfaceContainerLow.toArgb()
                }
            }
            val context = LocalContext.current
            val vm = koinViewModel<TransactionsViewModel>()
            val userId = context.getCurrentUserId()
            val transactions by vm.actions.getAllByUserIdFlow(userId)
                .collectAsState(initial = emptyList())
//            val transactions = produceState<List<Transaction>>(
//                initialValue = emptyList(),
//                key1 = userId
//            ) {
//                vm.actions.getAllByUserIdFlow(userId).collect { transactions ->
//                    value = transactions
//                }
//            }

            val view = LocalView.current
            val window = (view.context as Activity).window
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
                            if (transactions.isNotEmpty()) {
                                OverallChart(transactions.reversed())
                            }
                        }
                        composable(Routes.YearlyStats.route) {
                            if (transactions.isNotEmpty()) {
                                YearlyChart(transactions.reversed())
                            }
                        }
                        composable(Routes.QuarterlyStats.route) {
                            if (transactions.isNotEmpty()) {
                                QuarterlyChart(transactions.reversed())
                            }
                        }
                        composable(Routes.MonthlyStats.route) {
                            if (transactions.isNotEmpty()) {
                                MonthlyChart(transactions.reversed())
                            }
                        }
                    }
                    when (currentRoute) {
                        Routes.YearlyStats.route,
                        Routes.QuarterlyStats.route,
                        Routes.MonthlyStats.route -> {
                            window.navigationBarColor =
                                MaterialTheme.colorScheme.surfaceContainerLow.toArgb()
                        }

                        else -> {
                            window.navigationBarColor =
                                MaterialTheme.colorScheme.surface.toArgb()

                        }
                    }
                }
            }
        }
    }
}