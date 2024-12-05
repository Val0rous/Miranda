package com.cashflowtracker.miranda.ui.composables

import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.patrykandpatrick.vico.core.DefaultAlpha
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.data.database.Transaction
import com.cashflowtracker.miranda.utils.Currencies
import com.cashflowtracker.miranda.utils.TransactionType
import com.cashflowtracker.miranda.utils.formatAmountAsInt
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollState
import com.patrykandpatrick.vico.compose.component.shape.shader.fromBrush
import com.patrykandpatrick.vico.compose.component.shapeComponent
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.chart.values.AxisValuesOverrider
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

data class ChartItem(
    val date: ZonedDateTime,
    val balance: Double,
    val transactionId: UUID
)

@Composable
fun AreaChart(
    modifier: Modifier,
    transactions: List<Transaction>,
    initialBalance: Double = 0.0,
    chartLineColor: Color,
    chartAreaColor: Color,
    isScrollToEnd: Boolean = false
) {
    val chartItems = mutableListOf<ChartItem>()

//    val dateList = mutableListOf<ZonedDateTime>()
//    val balanceList = mutableListOf<Double>()
    var currentBalance = initialBalance
    transactions.forEach { item ->
        val date = ZonedDateTime.parse(item.createdOn, DateTimeFormatter.ISO_ZONED_DATE_TIME)
        val deltaAmount = when (item.type) {
            TransactionType.OUTPUT.name -> -item.amount
            TransactionType.INPUT.name -> item.amount
            else -> 0.0
        }
        currentBalance += deltaAmount
        chartItems.add(ChartItem(date, currentBalance, item.transactionId))
    }

    val refreshDataset = remember { mutableIntStateOf(0) }
    val modelProducer = remember { ChartEntryModelProducer() }
    val datasetForModel = remember { mutableStateListOf(listOf<FloatEntry>()) }
    val datasetLineSpec = remember { arrayListOf<LineChart.LineSpec>() }

    val scrollState = rememberChartScrollState()
    val pointShape = shapeComponent(
        shape = Shapes.pillShape,
        color = chartLineColor
    )

    LaunchedEffect(key1 = refreshDataset.intValue) {
        datasetForModel.clear()
        datasetLineSpec.clear()
        var xPos = 0f
        val dataPoints = arrayListOf<FloatEntry>()

        datasetLineSpec.add(
            LineChart.LineSpec(
                lineColor = chartLineColor.toArgb(),
                lineThicknessDp = 3f,
                lineBackgroundShader = DynamicShaders.fromBrush(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            chartAreaColor.copy(DefaultAlpha.LINE_BACKGROUND_SHADER_START),
                            chartAreaColor.copy(DefaultAlpha.LINE_BACKGROUND_SHADER_END)
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                ),
                point = pointShape,
                pointSizeDp = 12f,
            )
        )
        dataPoints.add(
            FloatEntry(
                x = xPos++,
                y = initialBalance.toFloat()
            )
        )
        chartItems.forEach { (date, balance) ->
            dataPoints.add(
                FloatEntry(
                    x = xPos /*date.toEpochSecond().toFloat()*/,
                    y = balance.toFloat()
                )
            )
            xPos += 1f
        }

        datasetForModel.add(dataPoints)
        modelProducer.setEntries(datasetForModel)
    }

    if (datasetForModel.isNotEmpty()) {
        ProvideChartStyle() {
            val marker = rememberMarker()
            Chart(
                modifier = modifier.padding(start = 1.dp),
                chart = lineChart(
                    lines = datasetLineSpec
                ),
                chartModelProducer = modelProducer,
                startAxis = rememberStartAxis(
                    title = "Top values",
                    tickLength = 2.dp,
                    valueFormatter = { value, _ ->
                        //value.toInt().toString()
                        formatAmountAsInt(value, Currencies.EUR)
                    },
                    itemPlacer = AxisItemPlacer.Vertical.default(
                        maxItemCount = 6
                    ),
                ),
                bottomAxis = rememberBottomAxis(
                    title = "Count of values",
                    tickLength = 2.dp,
                    valueFormatter = { value, _ ->
                        ((value.toInt()) + 1).toString()
                    },
                    guideline = null
                ),
                marker = marker,
                chartScrollState = scrollState,
                isZoomEnabled = true
            )
        }
    }
}

@Composable
fun AreaChartThumbnail(
    modifier: Modifier,
    transactions: List<Transaction>,
    initialBalance: Double = 0.0,
    width: Dp,
    chartLineColor: Color,
    chartAreaColor: Color
) {
    val dateList = mutableListOf<ZonedDateTime>()
    val balanceList = mutableListOf<Double>()
    var currentBalance = initialBalance
    transactions.forEach { item ->
        dateList.add(ZonedDateTime.parse(item.createdOn, DateTimeFormatter.ISO_ZONED_DATE_TIME))
        val deltaAmount = when (item.type) {
            TransactionType.OUTPUT.name -> -item.amount
            TransactionType.INPUT.name -> item.amount
            else -> 0.0
        }
        currentBalance += deltaAmount
        balanceList.add(currentBalance)
    }
    val dateBalanceList = dateList.zip(balanceList)

    val refreshDataset = remember { mutableIntStateOf(0) }
    val modelProducer = remember { ChartEntryModelProducer() }
    val datasetForModel = remember { mutableStateListOf(listOf<FloatEntry>()) }
    val datasetLineSpec = remember { arrayListOf<LineChart.LineSpec>() }

    LaunchedEffect(key1 = refreshDataset.intValue) {
        datasetForModel.clear()
        datasetLineSpec.clear()
        var xPos = 0f
        val dataPoints = arrayListOf<FloatEntry>()

        datasetLineSpec.add(
            LineChart.LineSpec(
                lineColor = chartLineColor.toArgb(),
                lineThicknessDp = 3f,
                lineBackgroundShader = DynamicShaders.fromBrush(
                    brush = Brush.verticalGradient(
                        listOf(
                            chartAreaColor.copy(DefaultAlpha.LINE_BACKGROUND_SHADER_START),
                            chartAreaColor.copy(DefaultAlpha.LINE_BACKGROUND_SHADER_END)
                        )
                    )
                ),
            )
        )
        dataPoints.add(
            FloatEntry(
                x = xPos++,
                y = initialBalance.toFloat()
            )
        )
        dateBalanceList.forEach { (date, balance) ->
            dataPoints.add(
                FloatEntry(
                    x = xPos /*date.toEpochSecond().toFloat()*/,
                    y = balance.toFloat()
                )
            )
            xPos += 1f
        }

        datasetForModel.add(dataPoints)
        modelProducer.setEntries(datasetForModel)
    }

    if (datasetForModel.isNotEmpty()) {
        ProvideChartStyle {
            Chart(
                modifier = modifier,
                chart = lineChart(
                    lines = datasetLineSpec,
                    axisValuesOverrider = AxisValuesOverrider.fixed(
                        minX = 0.5f,    // 0f has a little margin to the left
                        maxX = (transactions.size - 0.5).toFloat() // remove - 0.5 for a little margin to the right
                    ),
                    spacing = with(LocalDensity.current) {
                        (width.value / (transactions.size + 1)).toDp()
                    },
                ),
                chartModelProducer = modelProducer,
                isZoomEnabled = false,
//                horizontalLayout = HorizontalLayout.Segmented
            )
        }
    }
}