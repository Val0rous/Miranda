package com.cashflowtracker.miranda.ui.composables

import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import com.cashflowtracker.miranda.data.database.Transaction
import com.cashflowtracker.miranda.utils.TransactionType
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollState
import com.patrykandpatrick.vico.compose.component.marker.markerComponent
import com.patrykandpatrick.vico.compose.component.shape.shader.fromBrush
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


@Composable
fun AreaChart(
    modifier: Modifier,
    list: List<Transaction>
) {
    val dateList = mutableListOf<ZonedDateTime>()
    val balanceList = mutableListOf<Double>()
    var currentBalance = 0.0
    list.forEach { item ->
        dateList.add(ZonedDateTime.parse(item.dateTime, DateTimeFormatter.ISO_ZONED_DATE_TIME))
        val deltaAmount = when (item.type) {
            TransactionType.OUTPUT.type -> -item.amount
            TransactionType.INPUT.type -> item.amount
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

    val scrollState = rememberChartScrollState()

    LaunchedEffect(key1 = refreshDataset.intValue) {
        datasetForModel.clear()
        datasetLineSpec.clear()
        var xPos = 0f
        val dataPoints = arrayListOf<FloatEntry>()
        datasetLineSpec.add(
            LineChart.LineSpec(
                lineColor = Color.Blue.toArgb(),
                lineBackgroundShader = DynamicShaders.fromBrush(
                    brush = Brush.verticalGradient(
                        listOf(
                            Color.Blue.copy(DefaultAlpha.LINE_BACKGROUND_SHADER_START),
                            Color.Blue.copy(DefaultAlpha.LINE_BACKGROUND_SHADER_END)
                        )
                    )
                )
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
        ProvideChartStyle() {
            val marker = rememberMarker()
            Chart(
                modifier = modifier,
                chart = lineChart(
                    lines = datasetLineSpec
                ),
                chartModelProducer = modelProducer,
                startAxis = rememberStartAxis(
                    title = "Top values",
                    tickLength = 2.dp,
                    valueFormatter = { value, _ ->
                        value.toInt().toString()
                    },
                    itemPlacer = AxisItemPlacer.Vertical.default(
                        maxItemCount = 6
                    )
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