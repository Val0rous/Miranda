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
import com.cashflowtracker.miranda.ui.composables.RecurrenceListItem
import com.cashflowtracker.miranda.ui.composables.TransactionListItem
import com.cashflowtracker.miranda.ui.viewmodels.RecurrencesViewModel
import com.cashflowtracker.miranda.utils.Currencies
import com.cashflowtracker.miranda.utils.Repeats
import org.koin.androidx.compose.koinViewModel

@Composable
fun Recurrents() {
    val context = LocalContext.current
    val vm = koinViewModel<RecurrencesViewModel>()
    val userId = context.getCurrentUserId()
    val recurrences by vm.actions.getAllByUserIdFlow(userId).collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (recurrences.isNotEmpty()) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(recurrences) {
                    RecurrenceListItem(
                        type = it.type,
                        dateTime = it.reoccursOn,
                        source = it.source,
                        destination = it.destination,
                        amount = it.amount,
                        currency = Currencies.get(it.currency),
                        comment = it.comment,
                        repeat = Repeats.valueOf(it.repeatInterval),
                        onClick = {
                            val intent = Intent(context, ViewRecurrence::class.java)
                            intent.putExtra("recurrenceId", it.recurrenceId.toString())
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
                Text("No recurrents found", textAlign = TextAlign.Center)
            }
        }
    }
}