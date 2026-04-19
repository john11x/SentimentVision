// ui/components/SentimentTimelineChart.kt
package com.example.sentimentvision.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

@Composable
fun SentimentTimelineChart(
    data: List<Pair<String, Float>>,
    modifier: Modifier = Modifier
) {
    AndroidView(
        factory = { context ->
            LineChart(context).apply {
                description.isEnabled = false
                setTouchEnabled(true)
                isDragEnabled = true
                setScaleEnabled(true)
                setPinchZoom(true)

                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.valueFormatter = IndexAxisValueFormatter(data.map { it.first })
                xAxis.setLabelCount(7, true)
                xAxis.granularity = 1f

                axisLeft.axisMinimum = -1.5f
                axisLeft.axisMaximum = 1.5f
                axisRight.isEnabled = false
            }
        },
        update = { chart ->
            val entries = data.mapIndexed { index, pair ->
                Entry(index.toFloat(), pair.second)
            }

            val dataSet = LineDataSet(entries, "Sentiment Trend").apply {
                color = Color(0xFF6200EE).hashCode()
                setCircleColor(Color(0xFF6200EE).hashCode())
                lineWidth = 2f
                circleRadius = 4f
                setDrawCircleHole(false)
                valueTextSize = 10f
                mode = LineDataSet.Mode.CUBIC_BEZIER
            }

            chart.data = LineData(dataSet)
            chart.invalidate()
        },
        modifier = modifier
    )
}