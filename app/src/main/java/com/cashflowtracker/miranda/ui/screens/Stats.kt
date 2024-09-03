package com.cashflowtracker.miranda.ui.screens

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cashflowtracker.miranda.utils.Routes

@Composable
fun Stats() {
    val context = LocalContext.current

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
                onClick = {
                    val intent = Intent(context, ViewStatsCharts::class.java)
                    intent.putExtra("startDestination", Routes.OverallStats.route)
                    context.startActivity(intent)
                }
            )

            StatsCard(
                title = "Yearly Report",
                modifier = Modifier.weight(1f),
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
                onClick = {
                    val intent = Intent(context, ViewStatsCharts::class.java)
                    intent.putExtra("startDestination", Routes.QuarterlyStats.route)
                    context.startActivity(intent)
                }
            )

            StatsCard(
                title = "Monthly Report",
                modifier = Modifier.weight(1f),
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
    onClick: () -> Unit
) {
    OutlinedCard(
        modifier = modifier
            .padding(4.dp) // Reduced padding inside each card to bring cards closer
            .aspectRatio(1f)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow) // Use surfaceVariant color
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.TopStart // Align content to the top start
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
