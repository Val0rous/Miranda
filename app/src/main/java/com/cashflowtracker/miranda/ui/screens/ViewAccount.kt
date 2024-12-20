package com.cashflowtracker.miranda.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.data.database.Account
import com.cashflowtracker.miranda.data.repositories.LoginRepository.getCurrentUserId
import com.cashflowtracker.miranda.ui.composables.AlertDialogIconTitle
import com.cashflowtracker.miranda.ui.composables.AreaChart
import com.cashflowtracker.miranda.ui.composables.BalanceText
import com.cashflowtracker.miranda.ui.composables.IconWithBackground
import com.cashflowtracker.miranda.ui.composables.RecurrenceListItem
import com.cashflowtracker.miranda.ui.composables.TransactionListItem
import com.cashflowtracker.miranda.ui.theme.LocalCustomColors
import com.cashflowtracker.miranda.ui.theme.MirandaTheme
import com.cashflowtracker.miranda.ui.viewmodels.AccountsViewModel
import com.cashflowtracker.miranda.ui.viewmodels.RecurrencesViewModel
import com.cashflowtracker.miranda.ui.viewmodels.TransactionsViewModel
import com.cashflowtracker.miranda.utils.AccountType
import com.cashflowtracker.miranda.utils.Currencies
import com.cashflowtracker.miranda.utils.Repeats
import com.cashflowtracker.miranda.utils.formatDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flatMap
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel
import java.util.UUID

class ViewAccount : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val initialAccountId = UUID.fromString(intent.getStringExtra("accountId") ?: "")
//        println("initialAccountTitle: $initialAccountId")
        setContent {
            val context = LocalContext.current
            val userId = context.getCurrentUserId()
            val accountId by remember { mutableStateOf(initialAccountId) }
            val balanceVisible by remember {
                mutableStateOf(
                    intent.getBooleanExtra(
                        "balanceVisible",
                        false
                    )
                )
            }
            val coroutineScope = rememberCoroutineScope()
            val accountsVm = koinViewModel<AccountsViewModel>()
            val transactionsVm = koinViewModel<TransactionsViewModel>()
            val recurrencesVm = koinViewModel<RecurrencesViewModel>()
            var isLoaded by remember { mutableStateOf(false) }
            var account by remember { mutableStateOf<Account?>(null) }
            var isFavorite by remember { mutableStateOf(false) }
            var isDeleting by remember { mutableStateOf(false) }
            val openAlertDialog = remember { mutableStateOf(false) }

            LaunchedEffect(key1 = accountId, key2 = isDeleting) {
                if (!isDeleting) {
                    coroutineScope.launch(Dispatchers.IO) {
                        accountsVm.actions.getByAccountIdFlow(accountId)
                            .collect {
                                withContext(Dispatchers.Main) {
                                    account = it.also {
                                        if (!isDeleting) {
                                            isFavorite = it.isFavorite
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
                            title = { Text(stringResource(R.string.account)) },
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
                                label = { Text(stringResource(R.string.favorite)) },
                                icon = {
                                    Icon(
                                        imageVector = if (isFavorite) {
                                            ImageVector.vectorResource(R.drawable.ic_favorite_filled)
                                        } else {
                                            ImageVector.vectorResource(R.drawable.ic_favorite)
                                        },
                                        contentDescription = stringResource(R.string.favorite)
                                    )
                                },
                                onClick = {
                                    isFavorite = !isFavorite
                                    accountsVm.actions.toggleIsFavorite(
                                        accountId,
                                        userId,
                                        isFavorite
                                    )
                                }
                            )
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
                                    val intent =
                                        Intent(this@ViewAccount, EditAccount::class.java)
                                    intent.putExtra("accountId", accountId.toString())
                                    startActivity(intent)
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
                                    openAlertDialog.value = true
                                },
                            )
                        }
                    }
                ) { paddingValues ->
                    var index by remember { mutableIntStateOf(0) }
                    val transactions =
                        transactionsVm.actions.getAllByAccountIdFlow(accountId.toString())
                            .collectAsState(emptyList()).value
                    val recurrences =
                        recurrencesVm.actions.getAllByAccountIdFlow(accountId.toString())
                            .collectAsState(emptyList()).value
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    ) {
                        item {
                            if (isLoaded) {
                                if (!isDeleting) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .padding(start = 8.dp, bottom = 32.dp)
                                            .padding(top = 32.dp)
                                            .padding(horizontal = 16.dp)
                                    ) {
                                        IconWithBackground(
                                            icon = AccountType.getIcon(account!!.type),
                                            iconSize = 40.dp,
                                            iconColor = LocalCustomColors.current.icon,
                                            backgroundSize = 56.dp,
                                            backgroundColor = LocalCustomColors.current.surfaceTintBlue,
                                            contentDescription = account!!.type
                                        )

                                        Text(
                                            text = account!!.title,
                                            style = MaterialTheme.typography.headlineSmall,
                                            modifier = Modifier.padding(start = 22.dp)
                                        )
                                    }
                                    Card(
                                        colors = CardDefaults.cardColors(
                                            containerColor = LocalCustomColors.current.cardSurface
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp)
                                            .clip(RoundedCornerShape(24.dp))
                                    ) {
                                        Column(
                                            modifier = Modifier.padding(
                                                horizontal = 16.dp,
                                                vertical = 16.dp
                                            ),
                                            verticalArrangement = Arrangement.spacedBy(20.dp)
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.padding()
                                            ) {
                                                Text(text = stringResource(R.string.current_balance))
                                                Spacer(modifier = Modifier.weight(1f))
                                                BalanceText(
                                                    balance = account!!.balance,
                                                    isVisible = balanceVisible,
                                                    style = MaterialTheme.typography.titleMedium,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    textAlign = TextAlign.End
                                                )
                                            }
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.padding()
                                            ) {
                                                Text(text = stringResource(R.string.type))
                                                Spacer(modifier = Modifier.weight(1f))
                                                Text(
                                                    text = stringResource(
                                                        AccountType.getType(
                                                            account!!.type
                                                        )
                                                    ),
                                                    style = MaterialTheme.typography.titleMedium,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                    textAlign = TextAlign.End
                                                )
                                            }
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                modifier = Modifier.padding()
                                            ) {
                                                Text(text = stringResource(R.string.created_on))
                                                Spacer(modifier = Modifier.weight(1f))
                                                Text(
                                                    text = formatDate(account!!.createdOn),
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
                        item {
                            Row(
                                modifier = Modifier
                                    .padding(top = 12.dp)
                                    .padding(horizontal = 16.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (transactions.isNotEmpty()) {
                                    FilterChip(
                                        modifier = Modifier.weight(1f),
                                        onClick = { index = 0 },
                                        label = {
                                            Text(
                                                text = stringResource(R.string.stats),
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.weight(1f)
                                            )
                                        },
                                        selected = index == 0,
                                        border = BorderStroke(
                                            (0.1).dp,
                                            MaterialTheme.colorScheme.surfaceVariant
                                        ),
                                        colors = InputChipDefaults.inputChipColors(
                                            containerColor = LocalCustomColors.current.cardSurface,
                                            selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer
                                                .copy(alpha = 0.8f)
                                        )
                                    )
                                }
                                if (transactions.isNotEmpty()) {
                                    FilterChip(
                                        modifier = Modifier.weight(1f),
                                        onClick = { index = 1 },
                                        label = {
                                            Text(
                                                text = stringResource(R.string.recents),
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.weight(1f)
                                            )
                                        },
                                        selected = index == 1,
                                        border = BorderStroke(
                                            (0.1).dp,
                                            MaterialTheme.colorScheme.surfaceVariant
                                        ),
                                        colors = InputChipDefaults.inputChipColors(
                                            containerColor = LocalCustomColors.current.cardSurface,
                                            selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer
                                                .copy(alpha = 0.8f)
                                        )
                                    )
                                }
                                if (recurrences.isNotEmpty()) {
                                    FilterChip(
                                        modifier = Modifier.weight(1f),
                                        onClick = { index = 2 },
                                        label = {
                                            Text(
                                                text = stringResource(R.string.recurrents),
                                                textAlign = TextAlign.Center,
                                                modifier = Modifier.weight(1f)
                                            )
                                        },
                                        selected = index == 2,
                                        border = BorderStroke(
                                            (0.1).dp,
                                            MaterialTheme.colorScheme.surfaceVariant
                                        ),
                                        colors = InputChipDefaults.inputChipColors(
                                            containerColor = LocalCustomColors.current.cardSurface,
                                            selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer
                                                .copy(alpha = 0.8f)
                                        )
                                    )
                                }
                            }
                        }
                        when (index) {
                            0 -> {
                                if (transactions.isNotEmpty()) {
                                    item {
                                        AreaChart(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(start = 0.dp)
                                                .padding(bottom = 16.dp),
                                            transactions = transactions,
                                            currency = Currencies.get(Currencies.EUR.name),
                                            chartLineColor = LocalCustomColors.current.chartLineBlue,
                                            chartAreaColor = LocalCustomColors.current.chartAreaBlue,
                                            pointSize = 0f
                                        )
                                    }
                                }
                            }

                            1 -> {
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
                                            val intent =
                                                Intent(context, ViewTransaction::class.java)
                                            intent.putExtra(
                                                "transactionId",
                                                it.transactionId.toString()
                                            )
                                            context.startActivity(intent)
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 0.dp)
                                    )
                                }
                            }

                            2 -> {
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
                                            val intent =
                                                Intent(context, ViewRecurrence::class.java)
                                            intent.putExtra(
                                                "recurrenceId",
                                                it.recurrenceId.toString()
                                            )
                                            context.startActivity(intent)
                                        }
                                    )
                                }
                            }
                        }
                    }
                    if (openAlertDialog.value) {
                        AlertDialogIconTitle(
                            icon = R.drawable.ic_delete,
                            onDismissRequest = {
                                openAlertDialog.value = false
                            },
                            onConfirmation = {
                                openAlertDialog.value = false
                                isDeleting = true
                                coroutineScope.launch {
                                    accountsVm.actions.removeAccount(accountId)
                                    finish()
                                }
                            },
                            dialogTitle = "Delete account",
                            dialogText = "This operation is irreversible",
                            actionText = "Delete"
                        )
                    }
                }
            }
        }
    }
}