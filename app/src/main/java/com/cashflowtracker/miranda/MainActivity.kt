package com.cashflowtracker.miranda

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cashflowtracker.miranda.data.repositories.LoginRepository.getCurrentUserId
import com.cashflowtracker.miranda.data.repositories.LoginRepository.getLoggedUserEmailOrNull
import com.cashflowtracker.miranda.ui.composables.ExpandableFAB
import com.cashflowtracker.miranda.ui.composables.ExtendedFAB
import com.cashflowtracker.miranda.ui.composables.MainTopAppBar
import com.cashflowtracker.miranda.ui.composables.Navbar
import com.cashflowtracker.miranda.ui.screens.AddRecurrence
import com.cashflowtracker.miranda.ui.screens.AddTransaction
import com.cashflowtracker.miranda.ui.screens.Home
import com.cashflowtracker.miranda.ui.screens.Login
import com.cashflowtracker.miranda.ui.screens.Recurrents
import com.cashflowtracker.miranda.ui.screens.Stats
import com.cashflowtracker.miranda.ui.screens.Transactions
import com.cashflowtracker.miranda.ui.theme.MirandaTheme
import com.cashflowtracker.miranda.ui.viewmodels.AccountsViewModel
import com.cashflowtracker.miranda.ui.viewmodels.RecurrencesViewModel
import com.cashflowtracker.miranda.ui.viewmodels.TransactionsViewModel
import com.cashflowtracker.miranda.ui.viewmodels.UsersViewModel
import com.cashflowtracker.miranda.utils.PermissionStatus
import com.cashflowtracker.miranda.utils.Routes
import com.cashflowtracker.miranda.utils.rememberPermission
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val initialStartDestination = intent.getStringExtra("startDestination") ?: Routes.Home.route
        setContent {
            val context = LocalContext.current
            val coroutineScope = rememberCoroutineScope()
            navController = rememberNavController()
            var startDestination by remember { mutableStateOf(initialStartDestination) }
            val userEmail by remember { mutableStateOf<String?>(context.getLoggedUserEmailOrNull()) }
            val isFabExpanded = remember { mutableStateOf(false) }
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
            val showProfileIconMenu = remember { mutableStateOf(false) }

            val usersVm = koinViewModel<UsersViewModel>()
            val state by usersVm.state.collectAsStateWithLifecycle()
            if (userEmail.isNullOrEmpty()) {
                val intent = Intent(
                    this@MainActivity,
                    Login::class.java
                )
                startActivity(intent)
                finish()
            } else {
                val userId = context.getCurrentUserId()
                val accountsVm = koinViewModel<AccountsViewModel>()
                val accounts by accountsVm.actions.getAllByUserId(userId)
                    .collectAsState(initial = emptyList())
                val totalBalance by accountsVm.actions.getTotalBalance(userId)
                    .collectAsState(initial = 0.0)
                val transactionsVm = koinViewModel<TransactionsViewModel>()
                val transactions by transactionsVm.actions.getAllByUserIdFlow(userId)
                    .collectAsState(initial = emptyList())
                val recurrencesVm = koinViewModel<RecurrencesViewModel>()
                val recurrences by recurrencesVm.actions.getAllByUserIdFlow(userId)
                    .collectAsState(initial = emptyList())

                val transactionsListState = rememberLazyListState()
                val recurrentsListState = rememberLazyListState()
                val isTransactionsFABExpanded = remember { mutableStateOf(true) }
                val isRecurrentsFABExpanded = remember { mutableStateOf(true) }

                MirandaTheme {
                    Scaffold(
                        modifier = modifier,
                        topBar = { MainTopAppBar(currentRoute, showProfileIconMenu) },
                        bottomBar = { Navbar(navController) },
                        floatingActionButton = {
                            when (currentRoute) {
                                Routes.Home.route -> {
                                    ExpandableFAB(isFabExpanded)
                                }

                                Routes.Transactions.route -> {
                                    ExtendedFAB(
                                        R.drawable.ic_assignment,
                                        stringResource(R.string.add_transaction),
                                        AddTransaction::class.java,
                                        transactionsListState,
                                        isTransactionsFABExpanded
                                    )
                                }

                                Routes.Recurrents.route -> {
                                    ExtendedFAB(
                                        R.drawable.ic_schedule,
                                        stringResource(R.string.add_recurrence),
                                        AddRecurrence::class.java,
                                        recurrentsListState,
                                        isRecurrentsFABExpanded
                                    )
                                }

                                else -> {
                                    /* No FAB */
                                }
                            }
                        },
                        floatingActionButtonPosition = FabPosition.End
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
                            composable(Routes.Home.route) {
                                Home(accounts, totalBalance)
                            }
                            composable(Routes.Transactions.route) {
                                Transactions(transactions, transactionsListState)
                            }
                            composable(Routes.Recurrents.route) {
                                Recurrents(recurrences, recurrentsListState)
                            }
                            composable(Routes.Stats.route) {
                                Stats(transactions)
                            }
                        }
                    }
                }

                val notificationPermission = rememberPermission(
                    permission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        Manifest.permission.POST_NOTIFICATIONS
                    } else {
                        Manifest.permission.ACCESS_NOTIFICATION_POLICY
                    }
                )
                LaunchedEffect(notificationPermission.status) {
                    if (notificationPermission.status != PermissionStatus.Granted) {
                        notificationPermission.launchPermissionRequest()
                    }
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save current navigation route
        outState.putString(
            "NAVIGATION_STATE",
            navController.currentBackStackEntry?.destination?.route
        )
    }
}
