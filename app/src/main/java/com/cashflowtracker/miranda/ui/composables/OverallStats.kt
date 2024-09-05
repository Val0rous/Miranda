package com.cashflowtracker.miranda.ui.composables

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.data.database.Transaction
import com.cashflowtracker.miranda.ui.theme.LocalCustomColors

@Composable
fun OverallChart(transactions: List<Transaction>) {
    if (transactions.isNotEmpty()) {
        AreaChart(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            transactions = transactions.reversed(),
            chartLineColor = LocalCustomColors.current.chartLineBlue,
            chartAreaColor = LocalCustomColors.current.chartAreaBlue
        )
    }
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            //.background(MaterialTheme.colorScheme.surfaceVariant)
//            .padding(16.dp),
//        contentAlignment = Alignment.Center
//    ) {
//    }
}
