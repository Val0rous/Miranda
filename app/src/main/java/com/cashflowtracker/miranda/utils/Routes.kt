package com.cashflowtracker.miranda.utils

sealed class Routes(val route: String) {
    data object Home : Routes("home")
    data object Transactions : Routes("transactions")
    data object Recurrents : Routes("recurrents")
    data object Stats : Routes("stats")
    data object OverallStats : Routes("overallStats")
    data object YearlyStats : Routes("yearlyStats")
    data object QuarterlyStats : Routes("quarterlyStats")
    data object MonthlyStats : Routes("monthlyStats")
}
