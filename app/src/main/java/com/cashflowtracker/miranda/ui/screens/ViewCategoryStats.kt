package com.cashflowtracker.miranda.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.data.repositories.LoginRepository.getCurrentUserId
import com.cashflowtracker.miranda.ui.composables.PieChart
import com.cashflowtracker.miranda.ui.theme.Green400
import com.cashflowtracker.miranda.ui.theme.LocalCustomColors
import com.cashflowtracker.miranda.ui.theme.MirandaTheme
import com.cashflowtracker.miranda.ui.theme.Red400
import com.cashflowtracker.miranda.ui.theme.Yellow400
import com.cashflowtracker.miranda.ui.viewmodels.TransactionsViewModel
import com.cashflowtracker.miranda.utils.CategoryClass
import com.cashflowtracker.miranda.utils.DefaultCategories
import com.cashflowtracker.miranda.utils.TransactionType
import org.koin.androidx.compose.koinViewModel

class ViewCategoryStats : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            val vm = koinViewModel<TransactionsViewModel>()
            val userId = context.getCurrentUserId()
            val transactions by vm.actions.getAllByUserIdFlow(userId)
                .collectAsState(initial = emptyList())

            val transactionOrder = listOf(
                TransactionType.OUTPUT.type,
                TransactionType.INPUT.type,
                TransactionType.TRANSFER.type
            )
            val transactionTypeCounts =
                transactions.groupingBy { it.type }.eachCount()
                    .filterKeys { it in transactionOrder }.mapValues { it.value }
                    .let { map -> transactionOrder.associateWith { map[it] ?: 0 } }

            val categoryOrder = listOf(
                CategoryClass.NECESSITY.name,
                CategoryClass.CONVENIENCE.name,
                CategoryClass.LUXURY.name
            )
            val transactionCategoryCounts =
                transactions.filter { it.type == TransactionType.OUTPUT.type }
                    .groupingBy { DefaultCategories.getType(it.destination).name }.eachCount()
                    .let { map -> categoryOrder.associateWith { map[it] ?: 0 } }


            MirandaTheme {
                Scaffold(
                    modifier = Modifier,
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(
                                    text = "Category Stats",
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            },
                            navigationIcon = {
                                IconButton(onClick = { finish() }) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.ic_arrow_back),
                                        contentDescription = "Back",
                                        tint = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            },
                            colors = TopAppBarDefaults.mediumTopAppBarColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            )
                        )
                    }
                ) { paddingValues ->
                    val customColors = LocalCustomColors.current
                    LazyColumn(
                        modifier = Modifier
                            .padding(paddingValues = paddingValues)
                            .fillMaxSize()
                    ) {
                        item {
                            PieChart(
                                data = transactionCategoryCounts,
                                radiusOuter = 50.dp,
                                colors = listOf(
//                                customColors.surfaceTintRed,
//                                customColors.surfaceTintYellow,
//                                customColors.surfaceTintGreen
                                    Red400,
                                    Yellow400,
                                    Green400
                                )
                            )
                        }

                        item {
                            PieChart(
                                data = transactionTypeCounts,
                                radiusOuter = 50.dp,
                                colors = listOf(
                                    customColors.surfaceTintRed,
                                    customColors.surfaceTintGreen,
                                    customColors.surfaceTintBlue
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}