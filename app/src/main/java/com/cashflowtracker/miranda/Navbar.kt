package com.cashflowtracker.miranda

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.cashflowtracker.miranda.ui.screens.Home
import com.cashflowtracker.miranda.ui.screens.Recurrents
import com.cashflowtracker.miranda.ui.screens.Stats
import com.cashflowtracker.miranda.ui.screens.Transactions

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
            route = Navigation.Home.route
        ),
        NavbarItem(
            label = "Transactions",
            icon = R.drawable.ic_assignment,
            filledIcon = R.drawable.ic_assignment_filled,
            route = Navigation.Transactions.route
        ),
        NavbarItem(
            label = "Recurrents",
            icon = R.drawable.ic_schedule,
            filledIcon = R.drawable.ic_schedule_filled,
            route = Navigation.Recurrents.route
        ),
        NavbarItem(
            label = "Stats",
            icon = R.drawable.ic_leaderboard,
            filledIcon = R.drawable.ic_leaderboard_filled,
            route = Navigation.Stats.route
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


//@Composable
//fun NavigationBar(navController: NavHostController) {
//    val items = listOf(
//        Navigation.Home,
//        Navigation.Transactions,
//        Navigation.Recurrents,
//        Navigation.Stats
//    )
//
//    NavigationBar {
//        val navBackStackEntry by navController.currentBackStackEntryAsState()
//        val currentRoute = navBackStackEntry?.destination?.route
//
//        items.forEach { item ->
//            NavigationBarItem(
//                icon = {
//                    Icon(
//                        imageVector = if (currentRoute == item.route) item.outlinedIcon
//                            ?: item.filledIcon else item.filledIcon,
//                        contentDescription = item.label
//                    )
//                },
//                label = { Text(item.label) },
//                selected = currentRoute == item.route,
//                onClick = {
//                    navController.navigate(item.route) {
//                        popUpTo(navController.graph.startDestinationId) {
//                            saveState = true
//                        }
//                        launchSingleTop = true
//                        restoreState = true
//                    }
//                }
//            )
//        }
//    }
//}

