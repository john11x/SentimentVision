// ui/components/MiniGamesGrid.kt
package com.example.sentimentvision.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.sentimentvision.ui.analytics.GameType

data class GameItem(
    val type: GameType,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val color: androidx.compose.ui.graphics.Color
)

@Composable
fun MiniGamesGrid(
    onGameSelected: (GameType) -> Unit,
    modifier: Modifier = Modifier
) {
    val games = listOf(
        GameItem(
            type = GameType.EMOTION_MATCH,
            title = "Emotion Match",
            description = "Match emotions to scenarios",
            icon = Icons.Default.Psychology,
            color = MaterialTheme.colorScheme.primary
        ),
        GameItem(
            type = GameType.MOOD_PREDICTOR,
            title = "Mood Predictor",
            description = "Predict tomorrow's mood",
            icon = Icons.Default.Timeline,
            color = MaterialTheme.colorScheme.secondary
        ),
        GameItem(
            type = GameType.BREATHING_EXERCISE,
            title = "Breathing Exercise",
            description = "Calm your mind",
            icon = Icons.Default.SelfImprovement,
            color = MaterialTheme.colorScheme.tertiary
        ),
        GameItem(
            type = GameType.POSITIVE_REFRAME,
            title = "Positive Reframe",
            description = "Transform negative thoughts",
            icon = Icons.Default.AutoAwesome,
            color = MaterialTheme.colorScheme.primaryContainer
        )
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        games.chunked(2).forEach { rowGames ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowGames.forEach { game ->
                    GameCard(
                        game = game,
                        onGameSelected = onGameSelected,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun GameCard(
    game: GameItem,
    onGameSelected: (GameType) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable { onGameSelected(game.type) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = game.color.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = game.icon,
                contentDescription = game.title,
                tint = game.color,
                modifier = Modifier.size(32.dp)
            )
            Text(
                text = game.title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = game.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}