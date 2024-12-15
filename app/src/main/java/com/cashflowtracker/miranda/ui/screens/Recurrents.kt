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
import com.cashflowtracker.miranda.data.database.Recurrence
import com.cashflowtracker.miranda.ui.composables.RecurrenceListItem
import com.cashflowtracker.miranda.utils.Currencies
import com.cashflowtracker.miranda.utils.Repeats

@Composable
fun Recurrents(recurrences: List<Recurrence>, recurrentsListState: LazyListState) {
    val context = LocalContext.current
//    val recurrencesVm = koinViewModel<RecurrencesViewModel>()
//    val userId = context.getCurrentUserId()
//    val recurrences by recurrencesVm.actions.getAllByUserIdFlow(userId).collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (recurrences.isNotEmpty()) {
            LazyColumn(
                state = recurrentsListState,
                modifier = Modifier.fillMaxSize()
            ) {
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
                        reoccursOn = it.reoccursOn,
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
                Text(stringResource(R.string.no_recurrents), textAlign = TextAlign.Center)
            }
        }
    }
}