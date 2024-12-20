package com.cashflowtracker.miranda.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.data.database.Transaction
import com.cashflowtracker.miranda.data.repositories.LoginRepository.getCurrentUserId
import com.cashflowtracker.miranda.ui.composables.AlertDialogIconTitle
import com.cashflowtracker.miranda.ui.composables.IsRecurrencePillCard
import com.cashflowtracker.miranda.ui.composables.MapViewer
import com.cashflowtracker.miranda.ui.composables.TransactionBubblesToFrom
import com.cashflowtracker.miranda.ui.composables.TransactionViewer
import com.cashflowtracker.miranda.ui.theme.MirandaTheme
import com.cashflowtracker.miranda.ui.viewmodels.AccountsViewModel
import com.cashflowtracker.miranda.ui.viewmodels.TransactionsViewModel
import com.cashflowtracker.miranda.utils.Coordinates
import com.cashflowtracker.miranda.utils.TransactionType
import com.cashflowtracker.miranda.utils.revertTransaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel
import java.util.UUID

class ViewTransaction : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val initialTransactionId = UUID.fromString(intent.getStringExtra("transactionId") ?: "")
        setContent {
            val userId = LocalContext.current.getCurrentUserId()
            val transactionId by remember { mutableStateOf(initialTransactionId) }
            val coroutineScope = rememberCoroutineScope()
            val vm = koinViewModel<TransactionsViewModel>()
            var isLoaded by remember { mutableStateOf(false) }
            var transaction by remember { mutableStateOf<Transaction?>(null) }
            var isDeleting by remember { mutableStateOf(false) }
            val openDeleteAlertDialog = remember { mutableStateOf(false) }
            val openEditAlertDialog = remember { mutableStateOf(false) }
            var sourceType by remember { mutableStateOf("") }
            var destinationType by remember { mutableStateOf("") }
            val accountsVm = koinViewModel<AccountsViewModel>()
            val isLocationLoaded = remember { mutableStateOf(false) }
            val coordinates = remember { mutableStateOf<Coordinates?>(null) }
            val isCreatedByRecurrence = remember { mutableStateOf(false) }

            LaunchedEffect(key1 = transactionId, key2 = isDeleting) {
                if (!isDeleting) {
                    coroutineScope.launch(Dispatchers.IO) {
                        vm.actions.getByTransactionIdFlow(transactionId)
                            .collect {
                                withContext(Dispatchers.Main) {
                                    transaction = it.also {
                                        if (!isDeleting) {
                                            isLocationLoaded.value = it.location.isNotEmpty()
                                            isCreatedByRecurrence.value = it.isCreatedByRecurrence

                                            coroutineScope.launch(Dispatchers.IO) {
                                                if (!isDeleting) {
                                                    if (it.type == TransactionType.OUTPUT.name
                                                        || it.type == TransactionType.TRANSFER.name
                                                    ) {
                                                        accountsVm.actions.getTypeByAccountId(
                                                            UUID.fromString(it.source)
                                                        ).collect { item ->
                                                            sourceType = item
                                                        }
                                                    }
                                                }
                                            }

                                            coroutineScope.launch(Dispatchers.IO) {
                                                if (!isDeleting) {
                                                    if (it.type == TransactionType.INPUT.name
                                                        || it.type == TransactionType.TRANSFER.name
                                                    ) {
                                                        accountsVm.actions.getTypeByAccountId(
                                                            UUID.fromString(it.destination)
                                                        ).collect { item ->
                                                            destinationType = item
                                                        }
                                                    }
                                                }
                                            }

                                            coroutineScope.launch {
                                                if (isLocationLoaded.value) {
                                                    it.location.split(", ", limit = 2)
                                                        .let { item ->
                                                            if (item.size == 2) {
                                                                coordinates.value =
                                                                    Coordinates(
                                                                        item[0].toDouble(),
                                                                        item[1].toDouble()
                                                                    )
                                                            }
                                                        }
                                                }
                                            }

                                            isLoaded = true
                                        }
                                    }
                                }
                            }
                    }
                }
            }

            MirandaTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = {
                                if (isLoaded) {
                                    if (!isDeleting) {
                                        Text(stringResource(TransactionType.getType(transaction!!.type)))
                                    }
                                }
                            },
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
                                label = { Text(stringResource(R.string.edit)) },
                                icon = {
                                    Icon(
                                        ImageVector.vectorResource(R.drawable.ic_edit),
                                        contentDescription = stringResource(R.string.edit)
                                    )
                                },
                                onClick = {
                                    if (!isCreatedByRecurrence.value) {
                                        val intent =
                                            Intent(
                                                this@ViewTransaction,
                                                EditTransaction::class.java
                                            )
                                        intent.putExtra("transactionId", transactionId.toString())
                                        startActivity(intent)
                                    } else {
                                        openEditAlertDialog.value = true
                                    }
                                }
                            )
                            NavigationBarItem(
                                selected = false,
                                label = { Text(stringResource(R.string.delete)) },
                                icon = {
                                    Icon(
                                        ImageVector.vectorResource(R.drawable.ic_delete),
                                        contentDescription = stringResource(R.string.delete)
                                    )
                                },
                                enabled = !isDeleting,
                                onClick = {
                                    openDeleteAlertDialog.value = true
                                },
                            )
                        }
                    }
                ) { paddingValues ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(vertical = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            if (isLoaded) {
                                if (!isDeleting) {
                                    TransactionBubblesToFrom(
                                        transaction!!,
                                        sourceType,
                                        destinationType
                                    )

//                                    HorizontalDivider(modifier = Modifier.padding(bottom = 24.dp))

                                    TransactionViewer(
                                        type = transaction!!.type,
                                        dateTime = transaction!!.createdOn,
                                        amount = transaction!!.amount,
                                        currency = transaction!!.currency,
                                        comment = transaction!!.comment,
                                        context = this@ViewTransaction
                                    )

//                                    HorizontalDivider(modifier = Modifier.padding(vertical = 24.dp))


                                    if (isCreatedByRecurrence.value) {
                                        IsRecurrencePillCard()
                                    }

                                    if (coordinates.value != null) {
                                        MapViewer(coordinates.value!!, isLocationLoaded)
                                    }
                                }
                            }
                        }
                    }

                    if (openDeleteAlertDialog.value) {
                        AlertDialogIconTitle(
                            icon = R.drawable.ic_delete,
                            onDismissRequest = {
                                openDeleteAlertDialog.value = false
                            },
                            onConfirmation = {
                                openDeleteAlertDialog.value = false
                                isDeleting = true
                                coroutineScope.launch(Dispatchers.IO) {
                                    revertTransaction(transaction!!, accountsVm, userId)
                                    vm.actions.removeTransaction(transactionId)
                                    finish()
                                }
                            },
                            dialogTitle = "Delete transaction",
                            dialogText = "This operation is irreversible.\nTransaction will be reverted upon deletion",
                            actionText = "Delete"
                        )
                    }

                    if (openEditAlertDialog.value) {
                        AlertDialogIconTitle(
                            icon = R.drawable.ic_schedule,
                            onDismissRequest = {
                                openEditAlertDialog.value = false
                            },
                            onConfirmation = {
                                val intent =
                                    Intent(
                                        this@ViewTransaction,
                                        EditTransaction::class.java
                                    )
                                intent.putExtra("transactionId", transactionId.toString())
                                openEditAlertDialog.value = false
                                startActivity(intent)
                            },
                            dialogTitle = "Recurrent transaction",
                            dialogText = "Editing this transaction will make it lose its recurrent status",
                            actionText = "Edit"
                        )
                    }
                }
            }
        }
    }
}