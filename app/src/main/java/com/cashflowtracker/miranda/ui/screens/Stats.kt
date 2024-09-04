package com.cashflowtracker.miranda.ui.screens

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cashflowtracker.miranda.data.database.Transaction
import com.cashflowtracker.miranda.data.repositories.LoginRepository.getCurrentUserId
import com.cashflowtracker.miranda.ui.composables.AreaChartThumbnail
import com.cashflowtracker.miranda.ui.theme.CustomColors
import com.cashflowtracker.miranda.ui.viewmodels.TransactionsViewModel
import com.cashflowtracker.miranda.utils.Routes
import org.koin.androidx.compose.koinViewModel

@Composable
fun Stats() {
    val context = LocalContext.current
    val vm = koinViewModel<TransactionsViewModel>()
    val userId = context.getCurrentUserId()
    val transactions by vm.actions.getAllByUserIdFlow(userId).collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp) // Reduced padding between rows
    ) {
        // First Row with Overall Cashflow and Yearly Report
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp) // Reduced padding between cards
        ) {
            StatsCard(
                title = "Overall Cashflow",
                modifier = Modifier.weight(1f),
                transactions = transactions,
                chartLineColor = CustomColors.current.chartLineBlue,
                chartAreaColor = CustomColors.current.chartAreaBlue,
                onClick = {
                    val intent = Intent(context, ViewStatsCharts::class.java)
                    intent.putExtra("startDestination", Routes.OverallStats.route)
                    context.startActivity(intent)
                }
            )

            StatsCard(
                title = "Yearly Report",
                modifier = Modifier.weight(1f),
                transactions = transactions,
                chartLineColor = CustomColors.current.chartLineRed,
                chartAreaColor = CustomColors.current.chartAreaRed,
                onClick = {
                    val intent = Intent(context, ViewStatsCharts::class.java)
                    intent.putExtra("startDestination", Routes.YearlyStats.route)
                    context.startActivity(intent)
                }
            )
        }

        // Second Row with Quarterly Report and Monthly Report
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp) // Reduced padding between cards
        ) {
            StatsCard(
                title = "Quarterly Report",
                modifier = Modifier.weight(1f),
                transactions = transactions,
                chartLineColor = CustomColors.current.chartLineGreen,
                chartAreaColor = CustomColors.current.chartAreaGreen,
                onClick = {
                    val intent = Intent(context, ViewStatsCharts::class.java)
                    intent.putExtra("startDestination", Routes.QuarterlyStats.route)
                    context.startActivity(intent)
                }
            )

            StatsCard(
                title = "Monthly Report",
                modifier = Modifier.weight(1f),
                transactions = transactions,
                chartLineColor = CustomColors.current.chartLineYellow,
                chartAreaColor = CustomColors.current.chartAreaYellow,
                onClick = {
                    val intent = Intent(context, ViewStatsCharts::class.java)
                    intent.putExtra("startDestination", Routes.MonthlyStats.route)
                    context.startActivity(intent)
                }
            )
        }

        // Third Row with Categories
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp) // Reduced padding between cards
        ) {
            StatsCard(
                title = "Categories",
                modifier = Modifier.weight(1f),
                transactions = transactions,
                chartLineColor = CustomColors.current.chartLineBlue,
                chartAreaColor = CustomColors.current.chartAreaBlue,
                onClick = {
//                    context.startActivity(Intent(context, CategoriesActivity::class.java))
                }
            )

            // Placeholder card to maintain equal row structure
            Spacer(
                modifier = Modifier
                    .weight(1f)
            )
        }
    }
}

@Composable
fun StatsCard(
    title: String,
    modifier: Modifier = Modifier,
    transactions: List<Transaction>,
    chartLineColor: Color,
    chartAreaColor: Color,
    onClick: () -> Unit
) {
    var width by remember { mutableStateOf(0.dp) }
    OutlinedCard(
        modifier = modifier
            .padding(4.dp) // Reduced padding inside each card to bring cards closer
            .aspectRatio(1f)
            .clickable { onClick() }
            .onGloballyPositioned { coordinates ->
                width = coordinates.size.width.dp
            },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow) // Use surfaceVariant color
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            //textAlign = Alignment.TopStart, // Align content to the top start
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )
        if (transactions.isNotEmpty()) {
            AreaChartThumbnail(
                modifier = modifier.fillMaxSize(),
                transactions = transactions.reversed(),
                width = width,
                chartLineColor = chartLineColor,
                chartAreaColor = chartAreaColor
            )
        }
    }
}
