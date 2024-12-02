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
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.data.database.Transaction
import com.cashflowtracker.miranda.ui.theme.LocalCustomColors
import com.cashflowtracker.miranda.utils.TransactionType
import java.time.LocalDate
import java.time.Month

fun nextMonth(month: Int, year: MutableIntState): Int {
    return when (month) {
        12 -> 1.also {
            year.intValue += 1
        }

        else -> month + 1
    }
}

fun previousMonth(month: Int, year: MutableIntState): Int {
    return when (month) {
        1 -> 12.also {
            year.intValue -= 1
        }

        else -> month - 1
    }
}

fun getMonthAbbreviation(month: Int): String {
    return if (month in 1..12) {
        Month.of(month).name.substring(0, 3).lowercase().replaceFirstChar { it.uppercase() }
    } else {
        ""
    }
}

@Composable
fun MonthlyChart(transactions: List<Transaction>) {
    val date = LocalDate.now()
    val month = remember { mutableIntStateOf(date.month.value) }
    val year = remember { mutableIntStateOf(date.year) }
//    var filteredTransactions by remember { mutableStateOf<List<Transaction>>(emptyList()) }
//    val monthYearKey = remember(month.intValue, year.intValue) {
//        "${year.intValue}-${String.format("%02d", month.intValue)}"
//    }

    val filteredTransactions by remember(month.intValue, year.intValue) {
        derivedStateOf {
            val monthYear = "${year.intValue}-${String.format("%02d", month.intValue)}"
            transactions.filter { transaction ->
                transaction.createdOn.startsWith(monthYear)
            }
        }
    }


//    LaunchedEffect(key1 = monthYearKey) {
//        filteredTransactions = transactions.filter { transaction ->
//            transaction.dateTime.startsWith(monthYearKey)
//        }
//    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        if (filteredTransactions.isNotEmpty()) {
            key(filteredTransactions) {
                var initialBalance = 0.0
                val firstFilteredTransaction = filteredTransactions.first()
                val beforeTransactions =
                    transactions.takeWhile { it != firstFilteredTransaction }

                beforeTransactions.forEach { item ->
                    val deltaAmount = when (item.type) {
                        TransactionType.OUTPUT.type -> -item.amount
                        TransactionType.INPUT.type -> item.amount
                        else -> 0.0
                    }
                    initialBalance += deltaAmount
                }

                AreaChart(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    transactions = filteredTransactions,
                    initialBalance = initialBalance,
                    chartLineColor = LocalCustomColors.current.chartLineYellow,
                    chartAreaColor = LocalCustomColors.current.chartAreaYellow
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                Text(
                    text = "No transactions in selected month",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        MonthSelector(month, year)
    }
}

@Composable
fun MonthSelector(month: MutableIntState, year: MutableIntState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainerLow),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = { year.intValue -= 1 },
            modifier = Modifier.padding(end = 16.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_keyboard_double_arrow_left),
                contentDescription = "Previous Year",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        IconButton(
            onClick = { month.intValue = previousMonth(month.intValue, year) },
            modifier = Modifier.padding(end = 16.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_keyboard_arrow_left),
                contentDescription = "Previous Month",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        InputChip(
            modifier = Modifier.padding(vertical = 4.dp),
            selected = false,
            onClick = { /*TODO*/ },
            label = {
                Text(
                    text = "${getMonthAbbreviation(month.intValue)} ${year.intValue}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            },
            colors = InputChipDefaults.inputChipColors(containerColor = MaterialTheme.colorScheme.surface)
        )

        IconButton(
            onClick = { month.intValue = nextMonth(month.intValue, year) },
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_keyboard_arrow_right),
                contentDescription = "Next Month",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }

        IconButton(
            onClick = { year.intValue += 1 },
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
