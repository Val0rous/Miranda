package com.cashflowtracker.miranda.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.R
import com.cashflowtracker.miranda.data.repositories.LoginRepository.getCurrentUserId
import com.cashflowtracker.miranda.ui.composables.DonutChart
import com.cashflowtracker.miranda.ui.composables.DonutChartData
import com.cashflowtracker.miranda.ui.composables.DonutChartDataCollection
import com.cashflowtracker.miranda.ui.composables.DotsIndicator
import com.cashflowtracker.miranda.ui.composables.PieChart
import com.cashflowtracker.miranda.ui.theme.Blue400
import com.cashflowtracker.miranda.ui.theme.Blue700
import com.cashflowtracker.miranda.ui.theme.CustomColorScheme
import com.cashflowtracker.miranda.ui.theme.Green400
import com.cashflowtracker.miranda.ui.theme.Green700
import com.cashflowtracker.miranda.ui.theme.Light_ChartLine_Blue
import com.cashflowtracker.miranda.ui.theme.LocalCustomColors
import com.cashflowtracker.miranda.ui.theme.MirandaTheme
import com.cashflowtracker.miranda.ui.theme.Red400
import com.cashflowtracker.miranda.ui.theme.Red700
import com.cashflowtracker.miranda.ui.theme.Yellow400
import com.cashflowtracker.miranda.ui.viewmodels.TransactionsViewModel
import com.cashflowtracker.miranda.utils.CategoryClass
import com.cashflowtracker.miranda.utils.DefaultCategories
import com.cashflowtracker.miranda.utils.TransactionType
import com.cashflowtracker.miranda.utils.toMoneyFormat
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
                TransactionType.OUTPUT.name,
                TransactionType.INPUT.name,
                TransactionType.TRANSFER.name
            )
            val transactionTypeCounts = transactions
                .groupingBy { it.type }
                .eachCount()
                .mapKeys { (key, _) -> TransactionType.getType(key) }
                .let { map ->
                    transactionOrder.associate { type ->
                        TransactionType.getType(type) to (map[TransactionType.getType(type)] ?: 0)
                    }
                }

            val categoryOrder = listOf(
                CategoryClass.NECESSITY.label,
                CategoryClass.CONVENIENCE.label,
                CategoryClass.LUXURY.label
            )
            val transactionCategoryCounts = transactions
                .filter { it.type == TransactionType.OUTPUT.name }
                .groupingBy { DefaultCategories.getType(it.destination).label }
                .eachCount()
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
                    val pagerState = rememberPagerState(pageCount = { 2 })
                    LazyColumn(
                        modifier = Modifier
                            .padding(paddingValues = paddingValues)
                            .fillMaxSize()
                    ) {
                        item {
                            ElevatedCard(
                                colors = CardDefaults.cardColors(
                                    containerColor = LocalCustomColors.current.cardSurface
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp)
                                    .padding(16.dp)
                                    .clip(RoundedCornerShape(24.dp))
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    HorizontalPager(
                                        state = pagerState,
                                        modifier = Modifier
                                            .weight(1f)
                                            .fillMaxWidth()
                                            .padding(top = 8.dp)
                                    ) { page ->
                                        when (page) {
                                            0 -> {
                                                PieChart(
                                                    data = transactionCategoryCounts,
                                                    radiusOuter = 50.dp,
                                                    colors = listOf(
                                                        Red400,
                                                        Yellow400,
                                                        Green400
                                                    )
                                                )
                                            }

                                            1 -> {
                                                PieChart(
                                                    data = transactionTypeCounts,
                                                    radiusOuter = 50.dp,
                                                    colors = listOf(
                                                        Red400,
                                                        Green400,
                                                        Blue400
                                                    )
                                                )
                                            }
                                        }
                                    }
                                    DotsIndicator(
                                        totalDots = 2,
                                        selectedIndex = pagerState.currentPage,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp)
                                    )
                                }
                            }
                        }

//                        item {
//                            val viewData = DonutChartDataCollection(
//                                listOf(
//                                    DonutChartData(
//                                        transactionCategoryCounts["Necessity"]!!.toFloat(),
//                                        Red400,
//                                        "Necessary"
//                                    ),
//                                    DonutChartData(
//                                        transactionCategoryCounts["Convenience"]!!.toFloat(),
//                                        Yellow400,
//                                        "Convenience"
//                                    ),
//                                    DonutChartData(
//                                        transactionCategoryCounts["Luxury"]!!.toFloat(),
//                                        Green400,
//                                        "Luxury"
//                                    )
//                                )
//                            )
//                            DonutChart(
//                                Modifier.padding(paddingValues),
//                                chartSize = 200.dp,
//                                data = viewData
//                            ) { selected ->
//                                AnimatedContent(targetState = selected, label = "") {
//                                    val amount = it?.amount ?: viewData.totalAmount
//                                    val text = it?.title ?: "Total"
//
//                                    Column(
//                                        modifier = Modifier.fillMaxWidth(),
//                                        horizontalAlignment = Alignment.CenterHorizontally
//                                    ) {
//                                        Text(
//                                            "$amount",
//                                            style = MaterialTheme.typography.titleLarge,
//                                            color = MaterialTheme.colorScheme.onSurfaceVariant
//                                        )
//                                        Text(
//                                            text,
//                                            style = MaterialTheme.typography.titleMedium,
//                                            color = MaterialTheme.colorScheme.onSurface
//                                        )
//                                    }
//                                }
//                            }
//                        }
                    }
                }
            }
        }
    }
}