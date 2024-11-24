package com.cashflowtracker.miranda.utils

import androidx.compose.runtime.MutableState
import com.cashflowtracker.miranda.data.database.BaseTransaction

fun <T : BaseTransaction> getSuggestions(
    list: List<T>,
    transactionType: MutableState<String>,
    source: String,
    destination: String
): List<String> {
    return list
        .filter {
            when (transactionType.value) {
                TransactionType.OUTPUT.name -> it.type == TransactionType.OUTPUT.name && it.destination == destination
                TransactionType.INPUT.name -> it.type == TransactionType.INPUT.name && it.source == source
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