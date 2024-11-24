package com.cashflowtracker.miranda.ui.composables

import androidx.activity.compose.BackHandler
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.utils.Routes

data class NavbarItem(
    val label: Int,
    val icon: Int,
    val filledIcon: Int,
    val route: String
)

object NavbarItems {
    val items = listOf(
        NavbarItem(
            label = R.string.home,
            icon = R.drawable.ic_home,
            filledIcon = R.drawable.ic_home_filled,
            route = Routes.Home.route
        ),
        NavbarItem(
            label = R.string.transactions,
            icon = R.drawable.ic_assignment,
            filledIcon = R.drawable.ic_assignment_filled,
            route = Routes.Transactions.route
        ),
        NavbarItem(
            label = R.string.recurrents,
            icon = R.drawable.ic_schedule,
            filledIcon = R.drawable.ic_schedule_filled,
            route = Routes.Recurrents.route
        ),
        NavbarItem(
            label = R.string.stats,
            icon = R.drawable.ic_leaderboard,
            filledIcon = R.drawable.ic_leaderboard_filled,
            route = Routes.Stats.route
        )
    )
}


@Composable
fun Navbar(navController: NavHostController) {
    var navigationSelectedItem by remember { mutableIntStateOf(0) }

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination

    NavigationBar {
        NavbarItems.items.forEachIndexed { index, item ->
            if (currentDestination?.route == item.route) {
                navigationSelectedItem = index
            }
            val label = stringResource(item.label)
            NavigationBarItem(
                selected = index == navigationSelectedItem,
                label = { Text(label) },
                icon = {
                    Icon(
                        imageVector = if (index == navigationSelectedItem) {
                            ImageVector.vectorResource(item.filledIcon)
                        } else {
                            ImageVector.vectorResource(item.icon)
                        },
                        contentDescription = label
                    )
                },
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                    navigationSelectedItem = index
                }
            )
        }
    }
}
