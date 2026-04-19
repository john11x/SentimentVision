// ui/components/EmotionWheel.kt
package com.example.sentimentvision.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke

@Composable
fun EmotionWheel(
    emotions: Map<String, Float>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val radius = size.minDimension * 0.4f
            val center = Offset(size.width / 2, size.height / 2)

            val emotionColors = listOf(
                Color(0xFFFF6B6B), // Red - Anger
                Color(0xFF4ECDC4), // Teal - Fear
                Color(0xFFFFD166), // Yellow - Joy
                Color(0xFF06D6A0), // Green - Sadness
                Color(0xFF118AB2), // Blue - Surprise
            )

            var startAngle = -90f
            emotions.entries.forEachIndexed { index, (emotion, value) ->
                val sweepAngle = value * 360f
                val color = emotionColors.getOrNull(index) ?: Color.Gray

                drawArc(
                    color = color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = 30f)
                )

                startAngle += sweepAngle
            }
        }
    }
}