package com.cashflowtracker.miranda.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.data.database.Transaction
import com.cashflowtracker.miranda.data.repositories.LoginRepository.getCurrentUserId
import com.cashflowtracker.miranda.ui.theme.CustomColors
import com.cashflowtracker.miranda.ui.theme.Green400
import com.cashflowtracker.miranda.ui.theme.MirandaTheme
import com.cashflowtracker.miranda.ui.theme.Red400
import com.cashflowtracker.miranda.ui.theme.Yellow400
import com.cashflowtracker.miranda.ui.viewmodels.AccountsViewModel
import com.cashflowtracker.miranda.ui.viewmodels.TransactionsViewModel
import com.cashflowtracker.miranda.utils.AccountType
import com.cashflowtracker.miranda.utils.CategoryClass
import com.cashflowtracker.miranda.utils.DefaultCategories
import com.cashflowtracker.miranda.utils.SpecialType
import com.cashflowtracker.miranda.utils.formatZonedDateTime
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
            val vm = koinViewModel<TransactionsViewModel>()
            var transaction by remember {
                mutableStateOf(
                    Transaction(
                        UUID.fromString("00000000-0000-0000-0000-000000000000"),
                        "",
                        "",
                        "",
                        "",
                        0.0,
                        "",
                        "",
                        "",
                        UUID.fromString("00000000-0000-0000-0000-000000000000")
                    )
                )
            }
            val coroutineScope = rememberCoroutineScope()
            var isDeleting by remember { mutableStateOf(false) }
            val openAlertDialog = remember { mutableStateOf(false) }

            LaunchedEffect(key1 = transactionId, key2 = isDeleting) {
                if (!isDeleting) {
                    coroutineScope.launch(Dispatchers.IO) {
                        vm.actions.getByTransactionIdFlow(transactionId)
                            .collect { retrievedTransaction ->
                                withContext(Dispatchers.Main) {
                                    transaction = retrievedTransaction
                                    println("TRANSACTION ID: $transactionId")
                                    println("DATETIME: ${transaction.dateTime}")
                                }
                            }
                    }
                }
            }

            val accountsVm = koinViewModel<AccountsViewModel>()

            MirandaTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text(transaction.type) },
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
                                label = { Text("Edit") },
                                icon = {
                                    Icon(
                                        ImageVector.vectorResource(R.drawable.ic_edit),
                                        contentDescription = "Edit"
                                    )
                                },
                                onClick = {
//                                    val intent = Intent(this@ViewTransaction, EditTransaction::class.java)
//                                    intent.putExtra("transactionId", transactionId.toString())
//                                    startActivity(intent)
                                }
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
                                enabled = !isDeleting,
                                onClick = {
                                    openAlertDialog.value = true
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
                            if (!isDeleting) {
                                Row(
                                    verticalAlignment = Alignment.Top,
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(56.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    when (transaction.type) {
                                                        "Output" -> CustomColors.current.surfaceTintRed
                                                        "Input" -> CustomColors.current.surfaceTintGreen
                                                        else -> CustomColors.current.surfaceTintBlue
                                                    }
                                                )
                                        ) {
                                            Icon(
                                                imageVector = ImageVector.vectorResource(
                                                    when (transaction.type) {
                                                        "Output" -> AccountType.getIcon(
                                                            accountsVm.actions.getTypeByTitle(
                                                                transaction.source,
                                                                userId
                                                            )
                                                        )

                                                        "Input" -> {
                                                            when (transaction.source) {
                                                                SpecialType.POCKET.category, SpecialType.EXTRA.category -> SpecialType.getIcon(
                                                                    transaction.source
                                                                )

                                                                else -> DefaultCategories.getIcon(
                                                                    transaction.source
                                                                )
                                                            }
                                                        }

                                                        else -> AccountType.getIcon(
                                                            accountsVm.actions.getTypeByTitle(
                                                                transaction.source,
                                                                userId
                                                            )
                                                        )
                                                    }
                                                ),
                                                contentDescription = transaction.source,
                                                tint = CustomColors.current.icon,
                                                modifier = Modifier
                                                    .size(40.dp)
                                                    .align(Alignment.Center)
                                            )
                                        }
                                        Text(
                                            text = transaction.source,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        if (transaction.type == "Input") {
                                            Row() {
                                                when (DefaultCategories.getType(transaction.source)) {
                                                    CategoryClass.NECESSITY -> repeat(1) {
                                                        Icon(
                                                            imageVector = ImageVector.vectorResource(
                                                                R.drawable.ic_star_filled
                                                            ),
                                                            contentDescription = "",
                                                            tint = Red400,
                                                            modifier = Modifier
                                                                .size(24.dp)
                                                        )
                                                    }

                                                    CategoryClass.CONVENIENCE -> repeat(2) {
                                                        Icon(
                                                            imageVector = ImageVector.vectorResource(
                                                                R.drawable.ic_star_filled
                                                            ),
                                                            contentDescription = "",
                                                            tint = Yellow400,
                                                            modifier = Modifier
                                                                .size(24.dp)
                                                        )
                                                    }

                                                    CategoryClass.LUXURY -> repeat(3) {
                                                        Icon(
                                                            imageVector = ImageVector.vectorResource(
                                                                R.drawable.ic_star_filled
                                                            ),
                                                            contentDescription = "",
                                                            tint = Green400,
                                                            modifier = Modifier
                                                                .size(24.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.ic_east),
                                        contentDescription = "To",
                                        tint = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier
                                            .size(24.dp)
                                            .weight(1f)
                                    )

                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(56.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    when (transaction.type) {
                                                        "Output" -> CustomColors.current.surfaceTintRed
                                                        "Input" -> CustomColors.current.surfaceTintGreen
                                                        else -> CustomColors.current.surfaceTintBlue
                                                    }
                                                )
                                        ) {
                                            Icon(
                                                imageVector = ImageVector.vectorResource(
                                                    when (transaction.type) {
                                                        "Output" -> DefaultCategories.getIcon(
                                                            transaction.destination
                                                        )

                                                        "Input" -> AccountType.getIcon(
                                                            accountsVm.actions.getTypeByTitle(
                                                                transaction.destination,
                                                                userId
                                                            )
                                                        )

                                                        else -> AccountType.getIcon(
                                                            accountsVm.actions.getTypeByTitle(
                                                                transaction.destination,
                                                                userId
                                                            )
                                                        )
                                                    }
                                                ),
                                                contentDescription = transaction.destination,
                                                tint = CustomColors.current.icon,
                                                modifier = Modifier
                                                    .size(40.dp)
                                                    .align(Alignment.Center)
                                            )
                                        }
                                        Text(
                                            text = transaction.destination,
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                        if (transaction.type == "Output") {
                                            Row() {
                                                when (DefaultCategories.getType(transaction.destination)) {
                                                    CategoryClass.NECESSITY -> repeat(1) {
                                                        Icon(
                                                            imageVector = ImageVector.vectorResource(
                                                                R.drawable.ic_star_filled
                                                            ),
                                                            contentDescription = "",
                                                            tint = Red400,
                                                            modifier = Modifier
                                                                .size(24.dp)
                                                        )
                                                    }

                                                    CategoryClass.CONVENIENCE -> repeat(2) {
                                                        Icon(
                                                            imageVector = ImageVector.vectorResource(
                                                                R.drawable.ic_star_filled
                                                            ),
                                                            contentDescription = "",
                                                            tint = Yellow400,
                                                            modifier = Modifier
                                                                .size(24.dp)
                                                        )
                                                    }

                                                    CategoryClass.LUXURY -> repeat(3) {
                                                        Icon(
                                                            imageVector = ImageVector.vectorResource(
                                                                R.drawable.ic_star_filled
                                                            ),
                                                            contentDescription = "",
                                                            tint = Green400,
                                                            modifier = Modifier
                                                                .size(24.dp)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                                if (transaction.dateTime.isNotEmpty()) {
                                    Text(
                                        text = formatZonedDateTime(transaction.dateTime),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                Text(
                                    text = when (transaction.type) {
                                        "Output" -> "-%.2f €"
                                        "Input" -> "+%.2f €"
                                        else -> "%.2f €"
                                    }.format(transaction.amount),
                                    style = MaterialTheme.typography.headlineMedium,
                                    color = when (transaction.type) {
                                        "Output" -> CustomColors.current.surfaceTintRed
                                        "Input" -> CustomColors.current.surfaceTintGreen
                                        else -> CustomColors.current.surfaceTintBlue
                                    }
                                )

                                if (transaction.comment!!.isNotEmpty()) {
                                    Text(
                                        text = transaction.comment!!,
                                        style = MaterialTheme.typography.headlineMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                    }

                }
            }
        }
    }
}