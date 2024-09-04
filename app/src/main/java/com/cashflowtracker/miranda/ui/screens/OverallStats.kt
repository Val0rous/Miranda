package com.cashflowtracker.miranda.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cashflowtracker.miranda.ui.composables.AreaChart
import com.cashflowtracker.miranda.data.database.Transaction

@Composable
fun OverallChart(transactions: List<Transaction>) {
    println("Transactions list size: ${transactions.size}")
    if (transactions.isNotEmpty()) {
        AreaChart(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            list = transactions.reversed()
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
