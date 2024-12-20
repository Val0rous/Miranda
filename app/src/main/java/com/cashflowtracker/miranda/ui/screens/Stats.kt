package com.cashflowtracker.miranda.ui.screens

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.data.database.Transaction
import com.cashflowtracker.miranda.data.repositories.ThemeRepository.getThemePreferenceFlow
import com.cashflowtracker.miranda.ui.composables.AreaChartThumbnail
import com.cashflowtracker.miranda.ui.theme.LocalCustomColors
import com.cashflowtracker.miranda.utils.Routes
import com.cashflowtracker.miranda.utils.StarWithBorder
import com.cashflowtracker.miranda.utils.TransactionType
import java.time.LocalDate
import java.time.Month
import java.time.format.DateTimeFormatter

@Composable
fun Stats(transactions: List<Transaction>) {
    val context = LocalContext.current
//    val transactionsVm = koinViewModel<TransactionsViewModel>()
//    val userId = context.getCurrentUserId()
//    val transactions by transactionsVm.actions.getAllByUserIdFlow(userId).collectAsState(initial = emptyList())

    val today = LocalDate.now()
    val dateFormatter = DateTimeFormatter.ISO_ZONED_DATE_TIME

    val date365DaysAgo = today.minusDays(365)
    val date90DaysAgo = today.minusDays(90)
    val date30DaysAgo = today.minusDays(30)

    val transactionsWithParsedDates = transactions.mapNotNull { transaction ->
        val parsedDate = try {
            LocalDate.parse(transaction.createdOn, dateFormatter)
        } catch (e: Exception) {
            null
        }
        parsedDate?.let { transaction to it }
    }.sortedBy { it.second }

//    val currentYear = today.format(DateTimeFormatter.ofPattern("yyyy"))
//    val currentMonth = today.format(DateTimeFormatter.ofPattern("yyyy-MM"))
//    val currentQuarter = when (today.month) {
//        Month.JANUARY, Month.FEBRUARY, Month.MARCH -> "Q1"
//        Month.APRIL, Month.MAY, Month.JUNE -> "Q2"
//        Month.JULY, Month.AUGUST, Month.SEPTEMBER -> "Q3"
//        Month.OCTOBER, Month.NOVEMBER, Month.DECEMBER -> "Q4"
//        else -> ""
//    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp) // Reduced padding between rows
    ) {
        if (transactions.isNotEmpty()) {
            // First Row with Overall Cashflow and Yearly Report
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp) // Reduced padding between cards
            ) {
                StatsCard(
                    title = stringResource(R.string.overall_cashflow),
                    modifier = Modifier.weight(1f),
                    transactions = transactions,
                    chartLineColor = LocalCustomColors.current.chartLineBlue,
                    chartAreaColor = LocalCustomColors.current.chartAreaBlue,
                    onClick = {
                        val intent = Intent(context, ViewChartStats::class.java)
                        intent.putExtra("startDestination", Routes.OverallStats.route)
                        context.startActivity(intent)
                    }
                )


                val yearlyTransactions = transactionsWithParsedDates.filter { (_, date) ->
                    !date.isBefore(date365DaysAgo) && !date.isAfter(today)
                }
                val yearlyInitialBalance =
                    calculateInitialBalance(transactionsWithParsedDates, yearlyTransactions)
//                val yearlyTransactions = transactions.filter { transaction ->
//                    transaction.createdOn.startsWith(currentYear)
//                }
//                var yearlyInitialBalance = 0.0
//                val firstYearlyTransaction = yearlyTransactions.first()
//                val beforeYearlyTransactions =
//                    transactions.takeWhile { it != firstYearlyTransaction }
//                beforeYearlyTransactions.forEach { item ->
//                    val deltaAmount = when (item.type) {
//                        TransactionType.OUTPUT.type -> -item.amount
//                        TransactionType.INPUT.type -> item.amount
//                        else -> 0.0
//                    }
//                    yearlyInitialBalance += deltaAmount
//                }
                StatsCard(
                    title = stringResource(R.string.yearly_report),
                    modifier = Modifier.weight(1f),
                    transactions = yearlyTransactions.map { it.first }.reversed(),
                    initialBalance = yearlyInitialBalance,
                    chartLineColor = LocalCustomColors.current.chartLineRed,
                    chartAreaColor = LocalCustomColors.current.chartAreaRed,
                    onClick = {
                        val intent = Intent(context, ViewChartStats::class.java)
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
                val quarterlyTransactions = transactionsWithParsedDates.filter { (_, date) ->
                    !date.isBefore(date30DaysAgo) && !date.isAfter(today)
                }
                val quarterlyInitialBalance =
                    calculateInitialBalance(transactionsWithParsedDates, quarterlyTransactions)
//                val quarterlyTransactions = transactions.filter { transaction ->
//                    val transactionDate =
//                        LocalDate.parse(
//                            transaction.createdOn,
//                            DateTimeFormatter.ISO_ZONED_DATE_TIME
//                        )
//                    val transactionYear = transactionDate.year.toString()
//                    val transactionMonth = transactionDate.month
//                    transactionYear == currentYear && when (currentQuarter) {
//                        "Q1" -> transactionMonth in listOf(
//                            Month.JANUARY, Month.FEBRUARY, Month.MARCH
//                        )
//
//                        "Q2" -> transactionMonth in listOf(
//                            Month.APRIL, Month.MAY, Month.JUNE
//                        )
//
//                        "Q3" -> transactionMonth in listOf(
//                            Month.JULY, Month.AUGUST, Month.SEPTEMBER
//                        )
//
//                        "Q4" -> transactionMonth in listOf(
//                            Month.OCTOBER, Month.NOVEMBER, Month.DECEMBER
//                        )
//
//                        else -> false
//                    }
//                }
//                var quarterlyInitialBalance = 0.0
//                val firstQuarterlyTransaction = quarterlyTransactions.first()
//                val beforeQuarterlyTransactions =
//                    transactions.takeWhile { it != firstQuarterlyTransaction }
//                beforeQuarterlyTransactions.forEach { item ->
//                    val deltaAmount = when (item.type) {
//                        TransactionType.OUTPUT.type -> -item.amount
//                        TransactionType.INPUT.type -> item.amount
//                        else -> 0.0
//                    }
//                    quarterlyInitialBalance += deltaAmount
//                }
                StatsCard(
                    title = stringResource(R.string.quarterly_report),
                    modifier = Modifier.weight(1f),
                    transactions = quarterlyTransactions.map { it.first }.reversed(),
                    initialBalance = quarterlyInitialBalance,
                    chartLineColor = LocalCustomColors.current.chartLineGreen,
                    chartAreaColor = LocalCustomColors.current.chartAreaGreen,
                    onClick = {
                        val intent = Intent(context, ViewChartStats::class.java)
                        intent.putExtra("startDestination", Routes.QuarterlyStats.route)
                        context.startActivity(intent)
                    }
                )

                val monthlyTransactions = transactionsWithParsedDates.filter { (_, date) ->
                    !date.isBefore(date90DaysAgo) && !date.isAfter(today)
                }
                val monthlyInitialBalance =
                    calculateInitialBalance(transactionsWithParsedDates, monthlyTransactions)
//                val monthlyTransactions = transactions.filter { transaction ->
//                    transaction.createdOn.startsWith(currentMonth)
//                }
//                var monthlyInitialBalance = 0.0
//                val firstMonthlyTransaction = monthlyTransactions.firstOrNull()
//                val beforeMonthlyTransactions =
//                    transactions.takeWhile { it != firstMonthlyTransaction!! }
//                beforeMonthlyTransactions.forEach { item ->
//                    val deltaAmount = when (item.type) {
//                        TransactionType.OUTPUT.type -> -item.amount
//                        TransactionType.INPUT.type -> item.amount
//                        else -> 0.0
//                    }
//                    monthlyInitialBalance += deltaAmount
//                }
                StatsCard(
                    title = stringResource(R.string.monthly_report),
                    modifier = Modifier.weight(1f),
                    transactions = monthlyTransactions.map { it.first }.reversed(),
                    initialBalance = monthlyInitialBalance,
                    chartLineColor = LocalCustomColors.current.chartLineYellow,
                    chartAreaColor = LocalCustomColors.current.chartAreaYellow,
                    onClick = {
                        val intent = Intent(context, ViewChartStats::class.java)
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
                CategoriesCard(
                    title = stringResource(R.string.categories),
                    modifier = Modifier.weight(1f),
                    onClick = {
                        context.startActivity(Intent(context, ViewCategoryStats::class.java))
                    }
                )

                // Placeholder card to maintain equal row structure
                Spacer(
                    modifier = Modifier
                        .weight(1f)
                )
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(stringResource(R.string.no_stats))
            }
        }
    }
}

// Helper function to calculate the initial balance before the given transactions
private fun calculateInitialBalance(
    allTransactions: List<Pair<Transaction, LocalDate>>,
    periodTransactions: List<Pair<Transaction, LocalDate>>
): Double {
    if (periodTransactions.isEmpty()) {
        // If there are no transactions in the period, return the last known balance
        return allTransactions.fold(0.0) { balance, (transaction, _) ->
            balance + transaction.amountDelta()
        }
    }

    val firstTransactionDate = periodTransactions.first().second
    val transactionsBeforePeriod = allTransactions.filter { (_, date) ->
        date.isBefore(firstTransactionDate)
    }

    return transactionsBeforePeriod.fold(0.0) { balance, (transaction, _) ->
        balance + transaction.amountDelta()
    }
}

// Extension function to calculate the amount delta based on transaction type
private fun Transaction.amountDelta(): Double {
    return when (this.type) {
        TransactionType.OUTPUT.name -> -this.amount
        TransactionType.INPUT.name -> this.amount
        else -> 0.0
    }
}

@Composable
fun StatsCard(
    title: String,
    modifier: Modifier = Modifier,
    transactions: List<Transaction>,
    initialBalance: Double = 0.0,
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
        colors = CardDefaults.outlinedCardColors(containerColor = LocalCustomColors.current.cardSurface), // Use surfaceVariant color
        border = CardDefaults.outlinedCardBorder().copy(width = (0.5).dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            //textAlign = Alignment.TopStart, // Align content to the top start
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )
        if (transactions.isNotEmpty()) {
            key(LocalContext.current.getThemePreferenceFlow()) {
                AreaChartThumbnail(
                    modifier = modifier.fillMaxSize(),
                    transactions = transactions.reversed(),
                    initialBalance = initialBalance,
                    width = width,
                    chartLineColor = chartLineColor,
                    chartAreaColor = chartAreaColor
                )
            }
        }
    }
}

@Composable
fun CategoriesCard(
    title: String,
    modifier: Modifier = Modifier,
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
        colors = CardDefaults.outlinedCardColors(containerColor = LocalCustomColors.current.cardSurface), // Use surfaceVariant color
        border = CardDefaults.outlinedCardBorder().copy(width = (0.5).dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 0.dp)
                .padding(bottom = 16.dp)
        ) {
            StarWithBorder(
                starLineColor = LocalCustomColors.current.starLineYellow,
                starAreaColor = LocalCustomColors.current.starAreaYellow,
                starSize = 96.dp,
            )
            Column {
                StarWithBorder(
                    starLineColor = LocalCustomColors.current.starLineGreen,
                    starAreaColor = LocalCustomColors.current.starAreaGreen,
                    starSize = 48.dp,
                    modifier = Modifier.offset(x = (-4).dp)
                )
                StarWithBorder(
                    starLineColor = LocalCustomColors.current.starLineRed,
                    starAreaColor = LocalCustomColors.current.starAreaRed,
                    starSize = 40.dp,
                    modifier = Modifier.offset(x = (-18).dp, y = (-8).dp)
                )
            }
        }
    }
}

