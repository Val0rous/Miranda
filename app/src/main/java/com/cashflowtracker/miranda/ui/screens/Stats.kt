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

@Composable
fun Stats() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // First Row with Overall Cashflow and Yearly Report
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatsCard(
                title = "Overall Cashflow",
                modifier = Modifier
                    .weight(1f)
                    .height(150.dp) // Set height to make cards narrower
            ) {
                //context.startActivity(Intent(context, OverallCashflowActivity::class.java))
            }

            StatsCard(
                title = "Yearly Report",
                modifier = Modifier
                    .weight(1f)
                    .height(150.dp) // Set height to make cards narrower
            ) {
                //context.startActivity(Intent(context, YearlyReportActivity::class.java))
            }
        }

        // Second Row with Quarterly Report and Monthly Report
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatsCard(
                title = "Quarterly Report",
                modifier = Modifier
                    .weight(1f)
                    .height(150.dp) // Set height to make cards narrower
            ) {
                //context.startActivity(Intent(context, QuarterlyReportActivity::class.java))
            }

            StatsCard(
                title = "Monthly Report",
                modifier = Modifier
                    .weight(1f)
                    .height(150.dp) // Set height to make cards narrower
            ) {
                //context.startActivity(Intent(context, MonthlyReportActivity::class.java))
            }
        }

        // Single row for Categories at the bottom
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatsCard(
                title = "Categories",
                modifier = Modifier
                    .weight(1f)
                    .height(150.dp) // Set height to make cards narrower
            ) {
                //context.startActivity(Intent(context, CategoriesActivity::class.java))
            }
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
            .width(150.dp) // Set a fixed width to make cards narrower
            .padding(8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant) // Use surfaceVariant color
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp)
            )
        }
    }
}
