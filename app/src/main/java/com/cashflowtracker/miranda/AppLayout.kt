package com.cashflowtracker.miranda

import android.content.Context
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.cashflowtracker.miranda.ui.screens.AddRecurrence
import com.cashflowtracker.miranda.ui.screens.AddTransaction
import com.cashflowtracker.miranda.ui.screens.Home
import com.cashflowtracker.miranda.ui.screens.Login
import com.cashflowtracker.miranda.ui.screens.Profile
import com.cashflowtracker.miranda.ui.screens.Recurrents
import com.cashflowtracker.miranda.ui.screens.Settings
import com.cashflowtracker.miranda.ui.screens.Signup
import com.cashflowtracker.miranda.ui.screens.Stats
import com.cashflowtracker.miranda.ui.screens.Transactions
import com.cashflowtracker.miranda.ui.viewmodels.UsersActions
import com.cashflowtracker.miranda.ui.viewmodels.UsersState
import kotlinx.coroutines.CoroutineScope

@Composable
fun AppLayout(
    navController: NavHostController,
    startDestination: String,
    state: UsersState,
    actions: UsersActions,
    isDarkTheme: Boolean,
//    onThemeChange: (Boolean) -> Unit
    followSystem: Boolean,
    context: Context,
    coroutineScope: CoroutineScope
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Hide bottom bar in Settings, Profile, AddTransaction and AddRecurrence
    val showBottomBar = currentRoute != Routes.Settings.route
            && currentRoute != Routes.Profile.route
            && currentRoute != Routes.AddTransaction.route
            && currentRoute != Routes.AddRecurrence.route
            && currentRoute != Routes.Signup.route
            && currentRoute != Routes.Login.route

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
            startDestination = startDestination,
            modifier = Modifier.padding(paddingValues = paddingValues)
        ) {
//            composable(Routes.Home.route) {
//                Home(navController)
//            }
            composable(Routes.Transactions.route) {
                Transactions(navController)
            }
            composable(Routes.Recurrents.route) {
                Recurrents(navController)
            }
            composable(Routes.Stats.route) {
                Stats(navController)
            }
//            composable(Routes.Settings.route) {
//                Settings(
//                    navController = navController,
//                    isDarkTheme = isDarkTheme,  // Passa lo stato corrente del tema
////                    onThemeChange = onThemeChange
//                    followSystem = followSystem,
//                    context = context,
//                    coroutineScope = coroutineScope
//                )
//            }
            composable(Routes.Profile.route) {
                Profile(navController)
            }
//            composable(Routes.AddTransaction.route) {
//                AddTransaction(navController)
//            }
            composable(Routes.AddRecurrence.route) {
                AddRecurrence(navController)
            }
//            composable(Routes.Login.route) {
//                Login(navController, state, actions, isDarkTheme)
//            }
//            composable(Routes.Signup.route) {
//                Signup(navController, state, actions, isDarkTheme)
//            }
        }
    }
}
