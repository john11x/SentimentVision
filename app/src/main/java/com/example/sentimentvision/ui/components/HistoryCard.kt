package com.example.sentimentvision.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.icons.filled.SentimentNeutral
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sentimentvision.data.model.SentimentResult
import com.example.sentimentvision.data.model.SentimentType
import com.example.sentimentvision.ui.theme.NegativeRed
import com.example.sentimentvision.ui.theme.NeutralGray
import com.example.sentimentvision.ui.theme.PositiveGreen
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryCard(
    result: SentimentResult,
    modifier: Modifier = Modifier
) {
    val (icon, color) = when (result.sentiment) {
        SentimentType.POSITIVE -> Icons.Default.SentimentSatisfied to PositiveGreen
        SentimentType.NEGATIVE -> Icons.Default.SentimentDissatisfied to NegativeRed
        SentimentType.NEUTRAL -> Icons.Default.SentimentNeutral to NeutralGray
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = result.sentiment.name,
                    tint = color,
                    modifier = Modifier.size(32.dp)
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = result.text,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = formatTimestamp(result.timestamp),
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = result.sentiment.name,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = color
                )
                Text(
                    text = "${(result.confidence * 100).toInt()}%",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

private fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
