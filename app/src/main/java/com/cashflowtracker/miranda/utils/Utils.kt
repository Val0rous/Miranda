package com.cashflowtracker.miranda.utils

import androidx.compose.runtime.MutableState
import com.cashflowtracker.miranda.data.database.BaseTransaction
import com.cashflowtracker.miranda.data.database.Recurrence
import com.cashflowtracker.miranda.data.database.Transaction

fun <T : BaseTransaction> getSuggestions(
    list: List<T>,
    transactionType: MutableState<TransactionType?>,
    source: String,
    destination: String
): List<String> {
    return list
        .filter {
            when (transactionType.value) {
                TransactionType.OUTPUT -> TransactionType.valueOf(it.type).name == TransactionType.OUTPUT.name && it.destination == destination
                TransactionType.INPUT -> TransactionType.valueOf(it.type).name == TransactionType.INPUT.name && it.source == source
                else -> false
            }
        }
        .mapNotNull { it.comment }
        .filter { it.isNotBlank() }
        .groupingBy { it }
        .eachCount()
        .entries
        .sortedByDescending { it.value }
        .take(5)
        .map { it.key }
}