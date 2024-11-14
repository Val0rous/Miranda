package com.cashflowtracker.miranda.utils

import androidx.compose.runtime.MutableState
import com.cashflowtracker.miranda.data.database.BaseTransaction
import com.cashflowtracker.miranda.data.database.Recurrence
import com.cashflowtracker.miranda.data.database.Transaction

fun <T : BaseTransaction> getSuggestions(
    list: List<T>,
    transactionType: MutableState<String>,
    source: String,
    destination: String
): List<String> {
    return list.filter {
        when (transactionType.value) {
            TransactionType.OUTPUT.type -> it.type == TransactionType.OUTPUT.type && it.destination == destination
            TransactionType.INPUT.type -> it.type == TransactionType.INPUT.type && it.source == source
            else -> false
        }
    }
        .mapNotNull { it.comment }
        .groupingBy { it }
        .eachCount()
        .entries
        .sortedByDescending { it.value }
        .take(5)
        .map { it.key }
}