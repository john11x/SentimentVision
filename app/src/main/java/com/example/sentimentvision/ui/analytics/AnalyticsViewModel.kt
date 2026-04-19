package com.example.sentimentvision.ui.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sentimentvision.data.model.SentimentResult
import com.example.sentimentvision.data.model.SentimentType
import com.example.sentimentvision.data.repository.RepositoryProvider
import com.example.sentimentvision.ui.components.SentimentData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.roundToInt

// Data classes for analytics
data class Milestone(
    val title: String,
    val description: String,
    val progress: Float,
    val achieved: Boolean,
    val iconRes: String = "🎯"
)

data class AnalyticsUiState(
    val sentimentData: SentimentData = SentimentData(0, 0, 0),
    val weeklyTrend: List<Pair<String, Float>> = emptyList(),
    val emotionBreakdown: Map<String, Float> = emptyMap(),
    val totalEntries: Int = 0,
    val averageSentiment: Float = 0f,
    val milestones: List<Milestone> = emptyList(),
    val isLoading: Boolean = false
)

enum class GameType {
    EMOTION_MATCH,
    MOOD_PREDICTOR,
    BREATHING_EXERCISE,
    POSITIVE_REFRAME
}

class AnalyticsViewModel : ViewModel() {

    private val repository = RepositoryProvider.sentimentRepository

    private val _uiState = MutableStateFlow(AnalyticsUiState())
    val uiState: StateFlow<AnalyticsUiState> = _uiState.asStateFlow()

    // Navigation event handling
    private val _navigationEvent = MutableStateFlow<GameType?>(null)
    val navigationEvent: StateFlow<GameType?> = _navigationEvent.asStateFlow()

    fun loadAnalyticsData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            val history = repository.getHistory()
            val sentimentData = calculateSentimentData(history)
            val weeklyTrend = calculateWeeklyTrend(history)
            val emotionBreakdown = analyzeEmotions(history)
            val milestones = calculateMilestones(history)
            val averageSentiment = calculateAverageSentiment(history)

            _uiState.value = AnalyticsUiState(
                sentimentData = sentimentData,
                weeklyTrend = weeklyTrend,
                emotionBreakdown = emotionBreakdown,
                totalEntries = history.size,
                averageSentiment = averageSentiment,
                milestones = milestones,
                isLoading = false
            )
        }
    }

    fun onGameSelected(gameType: GameType) {
        _navigationEvent.value = gameType
    }

    fun clearNavigation() {
        _navigationEvent.value = null
    }

    private fun calculateSentimentData(history: List<SentimentResult>): SentimentData {
        val positive = history.count { it.sentiment == SentimentType.POSITIVE }
        val negative = history.count { it.sentiment == SentimentType.NEGATIVE }
        val neutral = history.count { it.sentiment == SentimentType.NEUTRAL }
        return SentimentData(positive, negative, neutral)
    }

    private fun calculateWeeklyTrend(history: List<SentimentResult>): List<Pair<String, Float>> {
        val calendar = Calendar.getInstance()
        val last7Days = (0..6).map { daysAgo ->
            calendar.time = Date()
            calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)

            val dateStr = when (daysAgo) {
                0 -> "Today"
                1 -> "Yesterday"
                else -> {
                    val dayName = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault())
                    dayName
                }
            }

            val dayStart = calendar.apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }.timeInMillis

            val dayEnd = dayStart + (24 * 60 * 60 * 1000) - 1

            val dayEntries = history.filter { it.timestamp in dayStart..dayEnd }
            val avgSentiment = if (dayEntries.isNotEmpty()) {
                dayEntries.map {
                    when(it.sentiment) {
                        SentimentType.POSITIVE -> 1.0
                        SentimentType.NEUTRAL -> 0.5
                        SentimentType.NEGATIVE -> 0.0
                    }
                }.average().toFloat()
            } else {
                0.5f // Default neutral for no entries
            }

            dateStr to avgSentiment
        }.reversed()

        return last7Days
    }

    private fun analyzeEmotions(history: List<SentimentResult>): Map<String, Float> {
        val emotionKeywords = mapOf(
            "Joy" to listOf("happy", "excited", "great", "wonderful", "love", "amazing", "good", "best"),
            "Sadness" to listOf("sad", "depressed", "unhappy", "miserable", "cry", "lost", "hurt", "alone"),
            "Anger" to listOf("angry", "mad", "frustrated", "annoyed", "hate", "upset", "rage"),
            "Fear" to listOf("scared", "afraid", "worried", "anxious", "nervous", "stress", "panic"),
            "Surprise" to listOf("surprised", "amazed", "shocked", "wow", "unexpected")
        )

        val totalMatches = emotionKeywords.values.sumOf { keywords ->
            history.count { entry ->
                keywords.any { keyword ->
                    entry.text.contains(keyword, ignoreCase = true)
                }
            }
        }.toFloat()

        return if (totalMatches > 0) {
            emotionKeywords.mapValues { (_, keywords) ->
                val count = history.count { entry ->
                    keywords.any { keyword ->
                        entry.text.contains(keyword, ignoreCase = true)
                    }
                }
                (count / totalMatches)
            }
        } else {
            emptyMap()
        }
    }

    private fun calculateAverageSentiment(history: List<SentimentResult>): Float {
        if (history.isEmpty()) return 0.5f
        return history.map {
            when(it.sentiment) {
                SentimentType.POSITIVE -> 1.0
                SentimentType.NEUTRAL -> 0.5
                SentimentType.NEGATIVE -> 0.0
            }
        }.average().toFloat()
    }

    private fun calculateMilestones(history: List<SentimentResult>): List<Milestone> {
        val totalEntries = history.size
        val positiveEntries = history.count { it.sentiment == SentimentType.POSITIVE }
        val consecutivePositive = calculateConsecutivePositive(history)

        return listOf(
            Milestone(
                "First Steps",
                "Make your first journal entry",
                progress = if (totalEntries > 0) 1f else 0f,
                achieved = totalEntries > 0,
                iconRes = "📝"
            ),
            Milestone(
                "Positive Mindset",
                "5 positive entries",
                progress = (positiveEntries.coerceAtMost(5) / 5f),
                achieved = positiveEntries >= 5,
                iconRes = "🌟"
            ),
            Milestone(
                "Consistent Journaler",
                "10 total entries",
                progress = (totalEntries.coerceAtMost(10) / 10f),
                achieved = totalEntries >= 10,
                iconRes = "📊"
            ),
            Milestone(
                "Emotion Explorer",
                "Identify 3 different emotions",
                progress = (calculateUniqueEmotions(history).coerceAtMost(3) / 3f),
                achieved = calculateUniqueEmotions(history) >= 3,
                iconRes = "🎭"
            )
        )
    }

    private fun calculateConsecutivePositive(history: List<SentimentResult>): Int {
        // Sort by timestamp and find longest consecutive positive streak
        val sorted = history.sortedBy { it.timestamp }
        var maxStreak = 0
        var currentStreak = 0

        for (entry in sorted) {
            if (entry.sentiment == SentimentType.POSITIVE) {
                currentStreak++
                maxStreak = maxOf(maxStreak, currentStreak)
            } else {
                currentStreak = 0
            }
        }
        return maxStreak
    }

    private fun calculateUniqueEmotions(history: List<SentimentResult>): Int {
        val emotions = mutableSetOf<String>()
        val emotionWords = mapOf(
            "joy" to listOf("happy", "joy", "excited", "great"),
            "sadness" to listOf("sad", "depressed", "unhappy"),
            "anger" to listOf("angry", "mad", "frustrated"),
            "fear" to listOf("scared", "afraid", "worried")
        )

        history.forEach { entry ->
            emotionWords.forEach { (emotion, words) ->
                if (words.any { entry.text.contains(it, ignoreCase = true) }) {
                    emotions.add(emotion)
                }
            }
        }
        return emotions.size
    }
}