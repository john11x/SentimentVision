package com.example.sentimentvision.ui.components

import android.graphics.Color as AndroidColor
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

/**
 * Simple Compose wrapper around MPAndroidChart's LineChart.
 *
 * @param entries a list of Pair(xLabel, yValue). xLabel is shown on X axis as string.
 */
@Composable
fun LineChartComposable(
    entries: List<Pair<String, Float>>,
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier
) {
    AndroidView(
        factory = { ctx ->
            LineChart(ctx).apply {
                layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                description.isEnabled = false
                setNoDataText("No chart data")
                axisRight.isEnabled = false
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                legend.isEnabled = false
                setBackgroundColor(AndroidColor.TRANSPARENT)
            }
        },
        update = { chart ->
            val lineEntries = entries.mapIndexed { idx, pair -> Entry(idx.toFloat(), pair.second) }
            val dataSet = LineDataSet(lineEntries, "Trend").apply {
                mode = LineDataSet.Mode.CUBIC_BEZIER
                setDrawCircles(true)
                setDrawValues(false)
                lineWidth = 2f
                color = AndroidColor.rgb(76, 175, 80) // greenish
                setCircleColor(AndroidColor.rgb(76, 175, 80))
            }
            val labels = entries.map { it.first }
            chart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            chart.data = LineData(dataSet)
            chart.invalidate()
        }
    )
}
