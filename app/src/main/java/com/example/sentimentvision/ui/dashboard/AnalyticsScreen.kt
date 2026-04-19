package com.example.sentimentvision.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sentimentvision.data.model.SentimentType
import com.example.sentimentvision.data.repository.RepositoryProvider
import com.example.sentimentvision.ui.components.LineChartComposable
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun AnalyticsScreen() {
    val repo = RepositoryProvider.sentimentRepository
    val scope = rememberCoroutineScope()
    var entries by remember { mutableStateOf(listOf<Pair<String, Float>>()) }

    LaunchedEffect(Unit) {
        scope.launch {
            val history = repo.getHistory()
            val dayToScores = mutableMapOf<Long, MutableList<Float>>()
            for (h in history) {
                val dayKey = h.timestamp / (24 * 60 * 60 * 1000L)
                val score = when (h.sentiment) {
                    SentimentType.POSITIVE -> 1f
                    SentimentType.NEUTRAL -> 0.5f
                    SentimentType.NEGATIVE -> 0f
                    else -> 0f // ✅ Added else branch to make when exhaustive
                }
                dayToScores.getOrPut(dayKey) { mutableListOf() }.add(score)
            }

            val todayKey = System.currentTimeMillis() / (24 * 60 * 60 * 1000L)
            val sdf = SimpleDateFormat("EEE", Locale.getDefault())
            val series = mutableListOf<Pair<String, Float>>()
            for (i in 6 downTo 0) {
                val dayKey = todayKey - i
                val dateMillis = dayKey * (24 * 60 * 60 * 1000L)
                val avg = dayToScores[dayKey]?.let { list -> list.average().toFloat() } ?: 0f
                val label = sdf.format(Date(dateMillis))
                series.add(label to avg)
            }
            entries = series
        }
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Mood Trend (last 7 days)", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(12.dp))
            Box(modifier = Modifier.height(220.dp).fillMaxWidth()) {
                LineChartComposable(entries = entries)
            }
        }
    }
}