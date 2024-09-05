package com.cashflowtracker.miranda.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.data.database.Transaction
import com.cashflowtracker.miranda.ui.theme.LocalCustomColors

@Composable
fun QuarterlyChart(transactions: List<Transaction>) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        if (transactions.isNotEmpty()) {
            AreaChart(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp),
                transactions = transactions.reversed(),
                chartLineColor = LocalCustomColors.current.chartLineGreen,
                chartAreaColor = LocalCustomColors.current.chartAreaGreen
            )
        }
        QuarterlySelector()
    }
}

@Composable
fun QuarterlySelector() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainerLow),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { /* Handle left double arrow click */ },
            modifier = Modifier.padding(end = 16.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_keyboard_double_arrow_left),
                contentDescription = "Previous Year",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        IconButton(
            onClick = { /* Handle left arrow click */ },
            modifier = Modifier.padding(end = 16.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_keyboard_arrow_left),
                contentDescription = "Previous Quarter",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        InputChip(
            modifier = Modifier.padding(vertical = 4.dp),
            selected = false,
            onClick = { /*TODO*/ },
            label = {
                Text(
                    text = "Q1 2024",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            },
            colors = InputChipDefaults.inputChipColors(containerColor = MaterialTheme.colorScheme.surface)
        )

        IconButton(
            onClick = { /* Handle right arrow click */ },
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_keyboard_arrow_right),
                contentDescription = "Next Quarter",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        IconButton(
            onClick = { /* Handle right double arrow click */ },
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_keyboard_double_arrow_right),
                contentDescription = "Next Year",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
