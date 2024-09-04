package com.cashflowtracker.miranda.ui.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.patrykandpatrick.vico.core.DefaultAlpha
import androidx.compose.ui.graphics.toArgb
import com.cashflowtracker.miranda.data.database.Transaction
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollState
import com.patrykandpatrick.vico.compose.component.shape.shader.fromBrush
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry


@Composable
fun AreaChart(

) {
    val list = listOf(1.0, 5.0, 3.0, 10.0, 4.0, 1.0)

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
        list.forEach { item ->
            dataPoints.add(FloatEntry(x = xPos, y = item.toFloat()))
            xPos += 1f
        }

        datasetForModel.add(dataPoints)
        modelProducer.setEntries(datasetForModel)
    }

    if (datasetForModel.isNotEmpty()) {
        ProvideChartStyle() {
            Chart(
                chart = lineChart(
                    lines = datasetLineSpec
                ),
                chartModelProducer = modelProducer,
                chartScrollState = scrollState,
                isZoomEnabled = true
            )
        }
    }
}