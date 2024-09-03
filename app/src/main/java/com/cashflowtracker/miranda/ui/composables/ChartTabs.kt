package com.cashflowtracker.miranda.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.cashflowtracker.miranda.utils.Routes

data class ChartTabsItem(
    val label: String,
    val route: String
)

object ChartTabsItems {
    val items = listOf(
        ChartTabsItem(
            label = "Overall",
            route = Routes.OverallStats.route
        ),
        ChartTabsItem(
            label = "Yearly",
            route = Routes.YearlyStats.route
        ),
        ChartTabsItem(
            label = "Quarterly",
            route = Routes.QuarterlyStats.route
        ),
        ChartTabsItem(
            label = "Monthly",
            route = Routes.MonthlyStats.route
        )
    )
}

@Composable
fun ChartTabs(navController: NavHostController, startDestination: String) {
    var selectedTabIndex by remember {
        mutableIntStateOf(
            when (startDestination) {
                Routes.OverallStats.route -> 0
                Routes.YearlyStats.route -> 1
                Routes.QuarterlyStats.route -> 2
                Routes.MonthlyStats.route -> 3
                else -> 0   // Default to "Overall" tab
            }
        )
    }

    TabRow(
        modifier = Modifier.fillMaxWidth(),
        selectedTabIndex = selectedTabIndex,
        //containerColor = MaterialTheme.colorScheme.surface,
        //contentColor = MaterialTheme.colorScheme.onSurface,
        indicator = { tabPositions ->
            Box(
                Modifier
                    .tabIndicatorOffset(tabPositions[selectedTabIndex])
                    .height(3.dp)
                    .padding(horizontal = 20.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp
                        )
                    )
            )
        }
    ) {
        ChartTabsItems.items.forEachIndexed { index, item ->
            Tab(
                selected = index == selectedTabIndex,
                onClick = {
                    selectedTabIndex = index
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                text = {
                    Text(
                        text = item.label,
                        color = if (selectedTabIndex == index) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        },
                        style = MaterialTheme.typography.titleSmall
//                        fontSize = 14.sp
                    )
                }
            )
        }
    }
}