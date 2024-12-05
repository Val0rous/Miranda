package com.cashflowtracker.miranda.ui.screens

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.data.database.Account
import com.cashflowtracker.miranda.data.repositories.LoginRepository.getCurrentUserId
import com.cashflowtracker.miranda.data.repositories.PreferencesRepository.getBalanceVisibility
import com.cashflowtracker.miranda.data.repositories.PreferencesRepository.setBalanceVisibility
import com.cashflowtracker.miranda.ui.composables.AccountsFilter
import com.cashflowtracker.miranda.ui.composables.BalanceText
import com.cashflowtracker.miranda.ui.composables.IconWithBackground
import com.cashflowtracker.miranda.ui.theme.LocalCustomColors
import com.cashflowtracker.miranda.ui.theme.Red400
import com.cashflowtracker.miranda.ui.viewmodels.AccountsViewModel
import com.cashflowtracker.miranda.utils.AccountType
import org.koin.androidx.compose.koinViewModel

@Composable
fun Home(accounts: List<Account>, totalBalance: Double) {
    val context = LocalContext.current
    var balanceVisible by remember { mutableStateOf(context.getBalanceVisibility()) }
    val showFilterDialog = remember { mutableStateOf(false) }
//    val accountsVm = koinViewModel<AccountsViewModel>()
//    val userId = context.getCurrentUserId()
//    val accounts by accountsVm.actions.getAllByUserId(userId).collectAsState(initial = emptyList())
//    val totalBalance by accountsVm.actions.getTotalBalance(userId).collectAsState(initial = 0.0)
    val filterSelections = remember {
        AccountType.entries.associate { it.name to mutableStateOf(false) }
    }

    val filteredAccounts = if (filterSelections.values.all { !it.value }) {
        accounts
    } else {
        accounts.filter { account ->
            filterSelections[account.type]?.value == true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        OutlinedCard(
            colors = CardDefaults.cardColors(containerColor = LocalCustomColors.current.cardSurface),
            border = CardDefaults.outlinedCardBorder().copy(width = (0.5).dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 16.dp, end = 16.dp)
        ) {
            Column(
                modifier = Modifier.padding(
                    top = 12.dp,
                    bottom = 20.dp,
                    start = 24.dp,
                    end = 16.dp
                ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Text(
                        text = stringResource(R.string.total_balance),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Spacer(Modifier.weight(1f))
                    IconButton(
                        onClick = {
                            balanceVisible = !balanceVisible
                            context.setBalanceVisibility(balanceVisible)
                        },
                    ) {
                        Icon(
                            imageVector = if (!balanceVisible) {
                                ImageVector.vectorResource(R.drawable.ic_visibility_off)
                            } else {
                                ImageVector.vectorResource(R.drawable.ic_visibility_filled)
                            },
                            contentDescription = stringResource(R.string.toggle_visibility),
                            tint = if (!balanceVisible) {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            } else {
                                MaterialTheme.colorScheme.primary
                            }
                        )
                    }
                }
                BalanceText(
                    balance = totalBalance,
                    isVisible = balanceVisible,
                    style = MaterialTheme.typography.displayMedium.copy(fontSize = 45.sp),
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 24.dp, bottom = 0.dp, start = 16.dp, end = 8.dp)
        ) {
            Text(
                text = stringResource(R.string.accounts),
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(Modifier.weight(1f))
            Row() {
                IconButton(
                    onClick = {
                        context.startActivity(Intent(context, AddAccount::class.java))
                    },
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_add),
                        contentDescription = stringResource(R.string.add_account),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(
                    onClick = {
                        // TODO
                    },
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_swap_vert),
                        contentDescription = stringResource(R.string.sort),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(
                    onClick = {
                        showFilterDialog.value = true
                    },
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_filter_list),
                        contentDescription = stringResource(R.string.filter),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
//        HorizontalDivider(
//            modifier = Modifier.padding(top = 0.dp),
//            thickness = (0.5).dp,
//            color = MaterialTheme.colorScheme.outlineVariant
//        )
        if (filteredAccounts.isNotEmpty()) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(filteredAccounts) { account ->
                    ListItem(
                        headlineContent = {
                            Text(
                                text = account.title,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        leadingContent = {
                            IconWithBackground(
                                icon = AccountType.getIcon(account.type),
                                iconSize = 24.dp,
                                iconColor = LocalCustomColors.current.icon,
                                backgroundSize = 40.dp,
                                backgroundColor = LocalCustomColors.current.surfaceTintBlue,
                                contentDescription = account.type
                            )
                        },
                        trailingContent = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ) {
                                BalanceText(
                                    balance = account.balance,
                                    isVisible = balanceVisible,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                                if (account.isFavorite) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.ic_favorite_filled),
                                        contentDescription = "",
                                        tint = Red400,
                                        modifier = Modifier.padding(start = 16.dp),
                                    )
                                }
                            }
                        },
                        modifier = Modifier.clickable {
                            val intent = Intent(context, ViewAccount::class.java)
                            intent.putExtra("accountId", account.accountId.toString())
                                .putExtra("balanceVisible", balanceVisible)
                            context.startActivity(intent)
                        }
                    )
//                    HorizontalDivider()
                }
            }
        } else {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(stringResource(R.string.no_accounts))
            }
        }
    }

    if (showFilterDialog.value) {
        AccountsFilter(showFilterDialog, filterSelections)
    }
}

@Composable
fun DefaultMessage(message: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = message,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}