package com.cashflowtracker.miranda.utils

sealed class Routes(val route: String) {
    data object Home : Routes("home")
    data object Transactions : Routes("transactions")
    data object Recurrents : Routes("recurrents")
    data object Stats : Routes("stats")
    data object Settings : Routes("settings")
    data object Profile : Routes("profile")
    data object AddTransaction : Routes("add_transaction")
    data object AddRecurrence : Routes("add_recurrence")
    data object Login : Routes("login")
    data object Signup : Routes("signup")
}
