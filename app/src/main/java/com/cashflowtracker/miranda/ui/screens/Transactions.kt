package com.cashflowtracker.miranda.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.data.database.Transaction
import com.cashflowtracker.miranda.ui.composables.TransactionListItem
import com.cashflowtracker.miranda.utils.Currencies

@Composable
fun Transactions(transactions: List<Transaction>, transactionsListState: LazyListState) {
    val context = LocalContext.current
//    val transactionsVm = koinViewModel<TransactionsViewModel>()
//    val userId = context.getCurrentUserId()
//    val transactions by transactionsVm.actions.getAllByUserIdFlow(userId).collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (transactions.isNotEmpty()) {
            LazyColumn(
                state = transactionsListState,
                modifier = Modifier.fillMaxSize()
            ) {
                items(transactions) {
                    TransactionListItem(
                        type = it.type,
                        dateTime = it.createdOn,
                        source = it.source,
                        destination = it.destination,
                        amount = it.amount,
                        currency = Currencies.get(it.currency),
                        comment = it.comment,
                        onClick = {
                            val intent = Intent(context, ViewTransaction::class.java)
                            intent.putExtra("transactionId", it.transactionId.toString())
                            context.startActivity(intent)
                        }
                    )
                }
            }
        } else {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(stringResource(R.string.no_transactions), textAlign = TextAlign.Center)
            }
        }
    }
}