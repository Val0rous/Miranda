package com.cashflowtracker.miranda.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.data.repositories.LoginRepository.getCurrentUserId
import com.cashflowtracker.miranda.ui.composables.AccountListItem
import com.cashflowtracker.miranda.ui.composables.CategoryDescriptionBottomSheet
import com.cashflowtracker.miranda.ui.composables.CategoryListItem
import com.cashflowtracker.miranda.ui.composables.SpecialListItem
import com.cashflowtracker.miranda.ui.theme.MirandaTheme
import com.cashflowtracker.miranda.ui.viewmodels.AccountsViewModel
import com.cashflowtracker.miranda.utils.AccountType
import com.cashflowtracker.miranda.utils.DefaultCategories
import com.cashflowtracker.miranda.utils.DescriptionCategory
import com.cashflowtracker.miranda.utils.SpecialType
import com.cashflowtracker.miranda.utils.TransactionType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

class SelectDestination : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //val initialAccountType = intent.getStringExtra("accountType") ?: ""
        setContent {
            //val accountType = remember { mutableStateOf(initialAccountType) }
            val transactionType by remember {
                mutableStateOf(
                    intent.getStringExtra("transactionType") ?: ""
                )
            }
            val context = LocalContext.current
            val vm = koinViewModel<AccountsViewModel>()
            val userId = context.getCurrentUserId()
            val accounts by vm.actions.getAllByUserId(userId).collectAsState(initial = emptyList())
            val showDialog = remember { mutableStateOf(false) }
            val selectedItem = remember { mutableStateOf<DescriptionCategory?>(null) }
            MirandaTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text(stringResource(R.string.destination)) },
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
                            },
                            actions = {
                                IconButton(
                                    onClick = { /* Logic to search */ },
                                    modifier = Modifier.padding(
                                        top = 16.dp,
                                        bottom = 16.dp,
                                        end = 16.dp
                                    )
                                ) {
                                    Icon(
                                        ImageVector.vectorResource(R.drawable.ic_search),
                                        contentDescription = "Search"
                                    )
                                }
                            }
                        )
                    }
                ) { paddingValues ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        if (transactionType == TransactionType.INPUT.name
                            || transactionType == TransactionType.TRANSFER.name
                        ) {
                            // User accounts list - not the enum types
                            items(accounts) { account ->
                                AccountListItem(
                                    account = account,
                                    modifier = Modifier.clickable {
                                        val resultIntent =
                                            Intent().putExtra(
                                                "destinationTitle",
                                                account.accountId.toString()
                                            )
                                                .putExtra(
                                                    "destinationIcon",
                                                    AccountType.getIcon(account.type).toString()
                                                )
                                        setResult(RESULT_OK, resultIntent)
                                        finish()
                                    }
                                )
                            }
                        }
                        if (transactionType == TransactionType.OUTPUT.name) {
                            items(SpecialType.entries) {
                                if (it.isDestination) {
                                    SpecialListItem(
                                        special = it,
                                        modifier = Modifier.combinedClickable(
                                            onClick = {
                                                val resultIntent = Intent()
                                                    .putExtra(
                                                        "destinationTitle",
                                                        it.name
                                                    )
                                                    .putExtra(
                                                        "destinationIcon",
                                                        it.icon.toString()
                                                    )
                                                setResult(RESULT_OK, resultIntent)
                                                finish()
                                            },
                                            onLongClick = {
                                                CoroutineScope(Dispatchers.Main).launch {
                                                    selectedItem.value = it
                                                    showDialog.value = true
                                                }
                                            }
                                        )
                                    )
                                }
                            }
                            items(DefaultCategories.entries) {
                                CategoryListItem(
                                    category = it,
                                    modifier = Modifier.combinedClickable(
                                        onClick = {
                                            val resultIntent = Intent()
                                                .putExtra("destinationTitle", it.name)
                                                .putExtra(
                                                    "destinationIcon",
                                                    it.icon.toString()
                                                )
                                            setResult(RESULT_OK, resultIntent)
                                            finish()
                                        },
                                        onLongClick = {
                                            CoroutineScope(Dispatchers.Main).launch {
                                                selectedItem.value = it
                                                showDialog.value = true
                                            }
                                        }
                                    )
                                )
                            }
                        }
                    }

                    if (showDialog.value) {
                        CategoryDescriptionBottomSheet(
                            showDialog,
                            selectedItem,
                            false,
                            this@SelectDestination
                        )
                    }
                }
            }
        }
    }
}