package com.cashflowtracker.miranda.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.data.repositories.LoginRepository.getCurrentUserId
import com.cashflowtracker.miranda.ui.theme.LocalCustomColors
import com.cashflowtracker.miranda.ui.viewmodels.TransactionsViewModel
import com.cashflowtracker.miranda.utils.DefaultCategories
import com.cashflowtracker.miranda.utils.SpecialType
import org.koin.androidx.compose.koinViewModel
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

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
                items(transactions) { transaction ->
                    ListItem(
                        overlineContent = {
                            Text(
                                text = ZonedDateTime.parse(
                                    transaction.createdOn,
                                    DateTimeFormatter.ISO_ZONED_DATE_TIME
                                ).format(DateTimeFormatter.ofPattern("yyyy-MM-dd  ·  HH:mm")),
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        },
                        leadingContent = {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when (transaction.type) {
                                            "Output" -> LocalCustomColors.current.surfaceTintRed
                                            "Input" -> LocalCustomColors.current.surfaceTintGreen
                                            else -> LocalCustomColors.current.surfaceTintBlue
                                        }
                                    )
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(
                                        when (transaction.type) {
                                            "Output" -> DefaultCategories.getIcon(transaction.destination)

                                            "Input" -> {
                                                when (transaction.source) {
                                                    SpecialType.POCKET.category, SpecialType.EXTRA.category -> SpecialType.getIcon(
                                                        transaction.source
                                                    )

                                                    else -> DefaultCategories.getIcon(transaction.source)
                                                }
                                            }

                                            else -> R.drawable.ic_sync
                                        }
                                    ),
                                    contentDescription = transaction.type,
                                    tint = LocalCustomColors.current.icon,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .align(Alignment.Center)
                                )
                            }
                        },
                        headlineContent = {
                            Text(
                                text = transaction.comment ?: "",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        },
                        trailingContent = {
                            Text(
                                text = when (transaction.type) {
                                    "Output" -> if (transaction.amount != 0.0) "-%.2f €" else "%.2f €"
                                    "Input" -> if (transaction.amount != 0.0) "+%.2f €" else "%.2f €"
                                    else -> "%.2f €"
                                }.format(transaction.amount),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
//                                when (transaction.type) {
//                                    "Output" -> CustomColors.current.surfaceTintRed
//                                    "Input" -> CustomColors.current.surfaceTintGreen
//                                    else -> CustomColors.current.surfaceTintBlue
//                                }
                            )
                        },
                        modifier = Modifier.clickable {
                            val intent = Intent(context, ViewTransaction::class.java)
                            intent.putExtra("transactionId", transaction.transactionId.toString())
                            context.startActivity(intent)
                        }
                    )
                    HorizontalDivider()
                }
            }
        } else {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text("Create an account to unlock transactions", textAlign = TextAlign.Center)
            }
        }

//        Box(modifier = Modifier.fillMaxSize()) {
//            Text(
//                text = "Transactions Screen",
//                modifier = Modifier.align(Alignment.Center)
//            )
//        }
    }
}