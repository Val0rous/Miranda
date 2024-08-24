package com.cashflowtracker.miranda.ui.screens

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.data.database.Account
import com.cashflowtracker.miranda.data.repositories.LoginRepository.getCurrentUserId
import com.cashflowtracker.miranda.ui.composables.BalanceText
import com.cashflowtracker.miranda.ui.theme.MirandaTheme
import com.cashflowtracker.miranda.ui.viewmodels.AccountsViewModel
import com.cashflowtracker.miranda.utils.AccountType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.util.UUID

class ViewAccount : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val initialAccountTitle = intent.getStringExtra("accountTitle") ?: ""
        println("initialAccountTitle: $initialAccountTitle")
        setContent {
            val userId = LocalContext.current.getCurrentUserId()
            val accountTitle by remember { mutableStateOf(initialAccountTitle) }
            val balanceVisible by remember {
                mutableStateOf(
                    intent.getBooleanExtra(
                        "balanceVisible",
                        false
                    )
                )
            }
            val vm = koinViewModel<AccountsViewModel>()
            var account by remember {
                mutableStateOf(
                    Account(
                        "",
                        "",
                        0.0,
                        "",
                        UUID.fromString("00000000-0000-0000-0000-000000000000"),
                        false
                    )
                )
            }
            var isFavorite by remember { mutableStateOf(false) }
            val coroutineScope = rememberCoroutineScope()

            LaunchedEffect(key1 = accountTitle) {
                coroutineScope.launch(Dispatchers.IO) {
                    account = vm.actions.getByTitle(accountTitle, userId)
                    isFavorite = account.isFavorite
                }
            }

            MirandaTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text("Account") },
                            navigationIcon = {
                                IconButton(
                                    onClick = { finish() },
                                    modifier = Modifier.padding(
                                        start = 0.dp,
                                        top = 16.dp,
                                        bottom = 16.dp
                                    )
                                ) {
                                    Icon(
                                        ImageVector.vectorResource(R.drawable.ic_arrow_back),
                                        contentDescription = "Back"
                                    )
                                }
                            }
                        )
                    },
                    bottomBar = {
                        NavigationBar(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer,
                        ) {
                            NavigationBarItem(
                                selected = false,
                                label = { Text("Favorite") },
                                icon = {
                                    Icon(
                                        imageVector = if (isFavorite) {
                                            ImageVector.vectorResource(R.drawable.ic_favorite_filled)
                                        } else {
                                            ImageVector.vectorResource(R.drawable.ic_favorite)
                                        },
                                        contentDescription = "Favorite"
                                    )
                                },
                                onClick = {
                                    isFavorite = !isFavorite
                                    vm.actions.toggleIsFavorite(accountTitle, userId, isFavorite)
                                }
                            )
                            NavigationBarItem(
                                selected = false,
                                label = { Text("Edit") },
                                icon = {
                                    Icon(
                                        ImageVector.vectorResource(R.drawable.ic_edit),
                                        contentDescription = "Edit"
                                    )
                                },
                                onClick = { /*TODO*/ }
                            )
                            NavigationBarItem(
                                selected = false,
                                label = { Text("Delete") },
                                icon = {
                                    Icon(
                                        ImageVector.vectorResource(R.drawable.ic_delete),
                                        contentDescription = "Delete"
                                    )
                                },
                                onClick = {
                                    vm.actions.removeAccount(account)
                                    finish()
                                },
                            )
                        }
                    }
                ) { paddingValues ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(vertical = 32.dp, horizontal = 24.dp)
                    ) {
                        item {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(bottom = 32.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(56.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.surfaceTint)
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(
                                            AccountType.getIcon(
                                                account.type
                                            )
                                        ),
                                        contentDescription = account.type,
                                        tint = MaterialTheme.colorScheme.surface,
                                        modifier = Modifier
                                            .size(40.dp)
                                            .align(Alignment.Center)
                                    )
                                }
                                Text(
                                    text = account.title,
                                    style = MaterialTheme.typography.headlineSmall,
                                    modifier = Modifier.padding(start = 22.dp)
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(bottom = 32.dp)
                            ) {
                                Text(text = "Current balance")
                                Spacer(modifier = Modifier.weight(1f))
                                BalanceText(
                                    balance = account.balance,
                                    isVisible = balanceVisible,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.End
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(bottom = 32.dp)
                            ) {
                                Text(text = "Type")
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = account.type,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.End
                                )
                            }
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(bottom = 32.dp)
                            ) {
                                Text(text = "Created on")
                                Spacer(modifier = Modifier.weight(1f))
                                Text(
                                    text = account.creationDate,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.End
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}