package com.example.sentimentvision.ui.analytics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sentimentvision.ui.components.MiniGamesGrid
import com.example.sentimentvision.ui.components.SentimentPieChart
import com.example.sentimentvision.ui.components.EmotionBreakdownChart
import com.example.sentimentvision.ui.components.WeeklyTrendChart
import com.example.sentimentvision.ui.components.MilestoneProgress

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhancedAnalyticsScreen(
    viewModel: AnalyticsViewModel = viewModel(),
    onBack: () -> Unit = {},
    onNavigateToBreathingExercise: () -> Unit = {},
    onNavigateToPositiveReframe: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val navigationEvent by viewModel.navigationEvent.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadAnalyticsData()
    }

    // Handle navigation events
    LaunchedEffect(navigationEvent) {
        when (navigationEvent) {
            GameType.BREATHING_EXERCISE -> {
                onNavigateToBreathingExercise()
                viewModel.clearNavigation()
            }
            GameType.POSITIVE_REFRAME -> {
                onNavigateToPositiveReframe()
                viewModel.clearNavigation()
            }
            else -> {}
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Advanced Analytics",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Sentiment Analytics",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Quick Stats Cards
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        title = "Total Entries",
                        value = uiState.totalEntries.toString(),
                        icon = Icons.Default.Assessment,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        title = "Avg. Mood",
                        value = "%.1f/10".format(uiState.averageSentiment * 10),
                        icon = Icons.Default.TrendingUp,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.weight(1f)
                    )
                }
            }



            // Emotion Breakdown
            item {
                AnalyticsSection(title = "Emotion Analysis") {
                    EmotionBreakdownChart(
                        emotions = uiState.emotionBreakdown,
                        modifier = Modifier.height(180.dp)
                    )
                }
            }

            // Mini Games Section
            item {
                AnalyticsSection(title = "Interactive Games") {
                    MiniGamesGrid(
                        onGameSelected = { gameType ->
                            viewModel.onGameSelected(gameType)
                        }
                    )
                }
            }

            // Progress Milestones
            item {
                AnalyticsSection(title = "Your Progress") {
                    MilestoneProgress(
                        milestones = uiState.milestones,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun AnalyticsSection(
    title: String,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            content()
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: androidx.compose.ui.graphics.Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}