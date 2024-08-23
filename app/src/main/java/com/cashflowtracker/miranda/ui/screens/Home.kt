package com.cashflowtracker.miranda.ui.screens

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.data.repositories.LoginRepository.getCurrentUserEmail
import com.cashflowtracker.miranda.data.repositories.LoginRepository.getCurrentUserId
import com.cashflowtracker.miranda.data.repositories.LoginRepository.getLoggedUserEmail
import com.cashflowtracker.miranda.data.repositories.LoginRepository.getLoggedUserId
import com.cashflowtracker.miranda.ui.composables.BalanceText
import com.cashflowtracker.miranda.ui.viewmodels.AccountsViewModel
import com.cashflowtracker.miranda.utils.AccountType
import org.koin.androidx.compose.koinViewModel

@Composable
fun Home() {
    var balanceVisible by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val vm = koinViewModel<AccountsViewModel>()
    val userId = context.getCurrentUserId()
    val accounts by vm.actions.getAllByUserId(userId).collectAsState(initial = emptyList())
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        OutlinedCard(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
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
                        "Total Balance",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Spacer(Modifier.weight(1f))
                    IconButton(
                        onClick = {
                            balanceVisible = !balanceVisible
                        },
                    ) {
                        Icon(
                            imageVector = if (!balanceVisible) {
                                ImageVector.vectorResource(R.drawable.ic_visibility_off)
                            } else {
                                ImageVector.vectorResource(R.drawable.ic_visibility_filled)
                            },
                            contentDescription = "Toggle Visibility",
                            tint = if (!balanceVisible) {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            } else {
                                MaterialTheme.colorScheme.primary
                            }
                        )
                    }
                }
                BalanceText(
                    text = "3470.00 €",
                    isVisible = balanceVisible,
                    style = MaterialTheme.typography.displayMedium.copy(fontSize = 45.sp),
                    textAlign = TextAlign.Center,
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 24.dp, bottom = 0.dp, start = 16.dp, end = 8.dp)
        ) {
            Text(
                "Accounts",
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
                        contentDescription = "Add Account",
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
                        contentDescription = "Sort",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(
                    onClick = {
                        // TODO
                    },
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_filter_list),
                        contentDescription = "Filter",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            if (accounts.isNotEmpty()) {
                items(accounts) { account ->
                    ListItem(
                        headlineContent = {
                            Text(
                                text = account.title,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        },
                        leadingContent = {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceTint)
                            ) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(AccountType.entries.find {
                                        it.type == account.type
                                    }?.icon ?: 0),
                                    contentDescription = account.type,
                                    tint = MaterialTheme.colorScheme.surface,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .align(Alignment.Center)
                                )
                            }
                        },
                        trailingContent = {
                            Text(
                                text = String.format("%.2f €", account.balance),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        },
                        modifier = Modifier.clickable {
//                            val resultIntent =
//                                Intent().putExtra("accountType", accountType.type)
//                                    .putExtra("accountIcon", accountType.icon.toString())
//                            setResult(Activity.RESULT_OK, resultIntent)
//                            finish()
                        }
                    )
                    HorizontalDivider()
                }
            }
        }
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