// ui/components/EmotionBreakdownChart.kt
package com.example.sentimentvision.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EmotionBreakdownChart(
    emotions: Map<String, Float>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (emotions.isEmpty()) {
            Text(
                text = "No emotion data yet",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            emotions.entries.sortedByDescending { it.value }.forEach { (emotion, percentage) ->
                EmotionProgressItem(
                    emotion = emotion,
                    percentage = percentage,
                    color = getEmotionColor(emotion)
                )
            }
        }
    }
}

@Composable
fun EmotionProgressItem(
    emotion: String,
    percentage: Float,
    color: Color
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = emotion,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "${(percentage * 100).toInt()}%",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
        LinearProgressIndicator(
            progress = percentage,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = color,
            trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
        )
    }
}

@Composable
private fun getEmotionColor(emotion: String): Color {
    return when (emotion.lowercase()) {
        "joy" -> Color(0xFFFFD166) // Yellow
        "sadness" -> Color(0xFF118AB2) // Blue
        "anger" -> Color(0xFFFF6B6B) // Red
        "fear" -> Color(0xFF06D6A0) // Green
        "surprise" -> Color(0xFF7209B7) // Purple
        else -> MaterialTheme.colorScheme.primary
    }
}