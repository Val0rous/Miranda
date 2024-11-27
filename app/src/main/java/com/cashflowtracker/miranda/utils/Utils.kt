package com.cashflowtracker.miranda.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.rememberTextMeasurer
import com.cashflowtracker.miranda.data.database.BaseTransaction
import java.time.format.TextStyle

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

@Composable
fun GetMeasuredWidth(text: String, textStyle: TextStyle): Int {
    val textMeasurer = rememberTextMeasurer()
    val textLayoutResult = textMeasurer.measure(AnnotatedString(text))
    return textLayoutResult.size.width
}