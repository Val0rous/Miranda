package com.cashflowtracker.miranda

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

data class NavbarItem(
    val label: String,
    val icon: Int,
    val filledIcon: Int,
    val route: String
)

object NavbarItems {
    val items = listOf(
        NavbarItem(
            label = "Home",
            icon = R.drawable.ic_home,
            filledIcon = R.drawable.ic_home_filled,
            route = Routes.Home.route
        ),
        NavbarItem(
            label = "Transactions",
            icon = R.drawable.ic_assignment,
            filledIcon = R.drawable.ic_assignment_filled,
            route = Routes.Transactions.route
        ),
        NavbarItem(
            label = "Recurrents",
            icon = R.drawable.ic_schedule,
            filledIcon = R.drawable.ic_schedule_filled,
            route = Routes.Recurrents.route
        ),
        NavbarItem(
            label = "Stats",
            icon = R.drawable.ic_leaderboard,
            filledIcon = R.drawable.ic_leaderboard_filled,
            route = Routes.Stats.route
        )
    )
}


@Composable
fun Navbar(navController: NavHostController) {
    var navigationSelectedItem by remember { mutableStateOf(0) }

    NavigationBar {
        NavbarItems.items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = index == navigationSelectedItem,
                label = { Text(item.label) },
                icon = {
                    Icon(
                        imageVector = if (index == navigationSelectedItem) {
                            ImageVector.vectorResource(item.filledIcon)
                        } else {
                            ImageVector.vectorResource(item.icon)
                        },
                        contentDescription = item.label
                    )
                },
                onClick = {
                    navigationSelectedItem = index
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}
