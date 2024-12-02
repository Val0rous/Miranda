package com.cashflowtracker.miranda.ui.composables

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.data.database.Transaction
import com.cashflowtracker.miranda.ui.theme.LocalCustomColors

@Composable
fun OverallChart(transactions: List<Transaction>) {
    AreaChart(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp),
        transactions = transactions,
        chartLineColor = LocalCustomColors.current.chartLineBlue,
        chartAreaColor = LocalCustomColors.current.chartAreaBlue
    )
}
