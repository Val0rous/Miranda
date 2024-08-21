package com.cashflowtracker.miranda.utils

sealed class Routes(val route: String) {
    data object Home : Routes("home")
    data object Transactions : Routes("transactions")
    data object Recurrents : Routes("recurrents")
    data object Stats : Routes("stats")
}
