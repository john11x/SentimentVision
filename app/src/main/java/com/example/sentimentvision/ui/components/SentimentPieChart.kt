package com.example.sentimentvision.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sentimentvision.data.model.SentimentType
import com.example.sentimentvision.ui.theme.NegativeRed
import com.example.sentimentvision.ui.theme.NeutralGray
import com.example.sentimentvision.ui.theme.PositiveGreen

data class SentimentData(
    val positive: Int,
    val negative: Int,
    val neutral: Int
)

@Composable
fun SentimentPieChart(
    data: SentimentData,
    modifier: Modifier = Modifier
) {
    val total = data.positive + data.negative + data.neutral

    if (total == 0) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No data yet",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
        return
    }

    val positiveAngle = (data.positive.toFloat() / total) * 360f
    val negativeAngle = (data.negative.toFloat() / total) * 360f
    val neutralAngle = (data.neutral.toFloat() / total) * 360f

    var animationPlayed by remember { mutableStateOf(false) }
    val animatedPositive by animateFloatAsState(
        targetValue = if (animationPlayed) positiveAngle else 0f,
        animationSpec = tween(1000),
        label = "positive"
    )
    val animatedNegative by animateFloatAsState(
        targetValue = if (animationPlayed) negativeAngle else 0f,
        animationSpec = tween(1000, delayMillis = 200),
        label = "negative"
    )
    val animatedNeutral by animateFloatAsState(
        targetValue = if (animationPlayed) neutralAngle else 0f,
        animationSpec = tween(1000, delayMillis = 400),
        label = "neutral"
    )

    LaunchedEffect(Unit) {
        animationPlayed = true
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val size = size.minDimension
                val strokeWidth = 40f

                // Positive
                if (animatedPositive > 0) {
                    drawArc(
                        color = PositiveGreen,
                        startAngle = -90f,
                        sweepAngle = animatedPositive,
                        useCenter = false,
                        topLeft = Offset(
                            (this.size.width - size) / 2,
                            (this.size.height - size) / 2
                        ),
                        size = Size(size, size),
                        style = Stroke(strokeWidth)
                    )
                }

                // Negative
                if (animatedNegative > 0) {
                    drawArc(
                        color = NegativeRed,
                        startAngle = -90f + animatedPositive,
                        sweepAngle = animatedNegative,
                        useCenter = false,
                        topLeft = Offset(
                            (this.size.width - size) / 2,
                            (this.size.height - size) / 2
                        ),
                        size = Size(size, size),
                        style = Stroke(strokeWidth)
                    )
                }

                // Neutral
                if (animatedNeutral > 0) {
                    drawArc(
                        color = NeutralGray,
                        startAngle = -90f + animatedPositive + animatedNegative,
                        sweepAngle = animatedNeutral,
                        useCenter = false,
                        topLeft = Offset(
                            (this.size.width - size) / 2,
                            (this.size.height - size) / 2
                        ),
                        size = Size(size, size),
                        style = Stroke(strokeWidth)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Legend
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            LegendItem("Positive", PositiveGreen, data.positive, total)
            LegendItem("Negative", NegativeRed, data.negative, total)
            LegendItem("Neutral", NeutralGray, data.neutral, total)
        }
    }
}

@Composable
private fun LegendItem(
    label: String,
    color: Color,
    count: Int,
    total: Int
) {
    val percentage = if (total > 0) (count.toFloat() / total * 100).toInt() else 0

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Canvas(modifier = Modifier.size(16.dp)) {
            drawCircle(color = color)
        }
        Text(
            text = "$label: $count ($percentage%)",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}