package com.cashflowtracker.miranda.ui.composables

import android.content.Context
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.ui.theme.LocalCustomColors
import com.cashflowtracker.miranda.utils.Routes

data class ChartTabsItem(
    val label: String,
    val route: String
)

object ChartTabsItems {

    @Composable
    fun getItems(): List<ChartTabsItem> {
        val context = LocalContext.current
        return listOf(
            ChartTabsItem(
                label = context.getString(R.string.overall),
                route = Routes.OverallStats.route
            ),
            ChartTabsItem(
                label = context.getString(R.string.yearly),
                route = Routes.YearlyStats.route
            ),
            ChartTabsItem(
                label = context.getString(R.string.quarterly),
                route = Routes.QuarterlyStats.route
            ),
            ChartTabsItem(
                label = context.getString(R.string.monthly),
                route = Routes.MonthlyStats.route
            )
        )
    }
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

    val customColors = LocalCustomColors.current
    val tabColor by remember {
        derivedStateOf {
            when (selectedTabIndex) {
                0 -> customColors.chartLineBlue
                1 -> customColors.chartLineRed
                2 -> customColors.chartLineGreen
                3 -> customColors.chartLineYellow
                else -> customColors.chartLineBlue
            }
        }
    }

    val context = LocalContext.current
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStackEntry?.destination

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
                        color = tabColor,
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp
                        )
                    )
            )
        }
    ) {
        ChartTabsItems.getItems().forEachIndexed { index, item ->
            if (currentDestination?.route == item.route) {
                selectedTabIndex = index
            }
            Tab(
                selected = index == selectedTabIndex,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                    selectedTabIndex = index
                },
                text = {
                    Text(
                        text = item.label,
                        color = if (selectedTabIndex == index) {
                            tabColor
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