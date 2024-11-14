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
import java.time.format.DateTimeFormatter

fun nextQuarter(quarter: Int, year: MutableIntState): Int {
    return when (quarter) {
        4 -> 1.also {
            year.intValue += 1
        }

        else -> quarter + 1
    }
}

fun previousQuarter(quarter: Int, year: MutableIntState): Int {
    return when (quarter) {
        1 -> 4.also {
            year.intValue -= 1
        }

        else -> quarter - 1
    }
}


@Composable
fun QuarterlyChart(transactions: List<Transaction>) {
    val date = LocalDate.now()
    val year = remember { mutableIntStateOf(date.year) }
    val quarter = remember {
        mutableIntStateOf(
            when (date.month) {
                Month.JANUARY, Month.FEBRUARY, Month.MARCH -> 1
                Month.APRIL, Month.MAY, Month.JUNE -> 2
                Month.JULY, Month.AUGUST, Month.SEPTEMBER -> 3
                Month.OCTOBER, Month.NOVEMBER, Month.DECEMBER -> 4
                else -> 0
            }
        )
    }

    val filteredTransactions by remember(quarter.intValue, year.intValue) {
        derivedStateOf {
            transactions.filter { transaction ->
                val transactionDate =
                    LocalDate.parse(transaction.createdOn, DateTimeFormatter.ISO_ZONED_DATE_TIME)
                val transactionYear = transactionDate.year.toString()
                val transactionMonth = transactionDate.month
                transactionYear == year.intValue.toString() && when (quarter.intValue) {
                    1 -> transactionMonth in listOf(
                        Month.JANUARY, Month.FEBRUARY, Month.MARCH
                    )

                    2 -> transactionMonth in listOf(
                        Month.APRIL, Month.MAY, Month.JUNE
                    )

                    3 -> transactionMonth in listOf(
                        Month.JULY, Month.AUGUST, Month.SEPTEMBER
                    )

                    4 -> transactionMonth in listOf(
                        Month.OCTOBER, Month.NOVEMBER, Month.DECEMBER
                    )

                    else -> false
                }
            }
        }
    }

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
                        .padding(16.dp),
                    transactions = filteredTransactions,
                    initialBalance = initialBalance,
                    chartLineColor = LocalCustomColors.current.chartLineGreen,
                    chartAreaColor = LocalCustomColors.current.chartAreaGreen
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
                    text = "No transactions in selected quarter",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        QuarterlySelector(quarter, year)
    }
}

@Composable
fun QuarterlySelector(quarter: MutableIntState, year: MutableIntState) {
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
            onClick = { quarter.intValue = previousQuarter(quarter.intValue, year) },
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
                    text = "Q${quarter.intValue} ${year.intValue}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            },
            colors = InputChipDefaults.inputChipColors(containerColor = MaterialTheme.colorScheme.surface)
        )

        IconButton(
            onClick = { quarter.intValue = nextQuarter(quarter.intValue, year) },
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.ic_keyboard_arrow_right),
                contentDescription = "Next Quarter",
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
