package com.cashflowtracker.miranda.ui.screens

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import com.cashflowtracker.miranda.data.repositories.LoginRepository.getCurrentUserId
import com.cashflowtracker.miranda.ui.composables.TransactionListItem
import com.cashflowtracker.miranda.ui.viewmodels.TransactionsViewModel
import com.cashflowtracker.miranda.utils.Currencies
import org.koin.androidx.compose.koinViewModel

@Composable
fun Transactions() {
    val context = LocalContext.current
    val vm = koinViewModel<TransactionsViewModel>()
    val userId = context.getCurrentUserId()
    val transactions by vm.actions.getAllByUserIdFlow(userId).collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (transactions.isNotEmpty()) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
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
                Text("No transactions found", textAlign = TextAlign.Center)
            }
        }
    }
}