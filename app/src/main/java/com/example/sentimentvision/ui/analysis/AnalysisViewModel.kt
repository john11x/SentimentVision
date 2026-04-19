// ui/analysis/AnalysisViewModel.kt
package com.example.sentimentvision.ui.analysis

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sentimentvision.data.model.SentimentResult
import com.example.sentimentvision.data.model.SentimentType
import com.example.sentimentvision.data.repository.RepositoryProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Calendar

// UI State for Journal/Analysis screen
data class AnalysisUiState(
    val currentText: String = "",
    val isAnalyzing: Boolean = false,
    val analysisResult: SentimentResult? = null,
    val recentEntries: List<SentimentResult> = emptyList(),
    val error: String? = null,
    val showAnalysisResult: Boolean = false,
    val streak: Int = 0
)

class AnalysisViewModel : ViewModel() {

    private val repository = RepositoryProvider.sentimentRepository

    private val _uiState = MutableStateFlow(AnalysisUiState())
    val uiState: StateFlow<AnalysisUiState> = _uiState.asStateFlow()

    init {
        loadRecentEntries()
    }

    fun onTextChanged(text: String) {
        _uiState.value = _uiState.value.copy(
            currentText = text,
            error = null,
            showAnalysisResult = false
        )
    }

    fun analyzeAndSave() {
        val text = _uiState.value.currentText.trim()

        if (text.isEmpty()) {
            _uiState.value = _uiState.value.copy(
                error = "Please enter some text to analyze"
            )
            return
        }

        if (text.length < 3) {
            _uiState.value = _uiState.value.copy(
                error = "Please enter at least 3 characters"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isAnalyzing = true,
                error = null
            )

            repository.analyzeSentiment(text).fold(
                onSuccess = { result ->
                    _uiState.value = _uiState.value.copy(
                        isAnalyzing = false,
                        analysisResult = result,
                        currentText = "",
                        showAnalysisResult = true
                    )
                    loadRecentEntries()
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isAnalyzing = false,
                        error = "Analysis failed: ${exception.message}",
                        showAnalysisResult = false
                    )
                }
            )
        }
    }

    fun clearAnalysis() {
        _uiState.value = _uiState.value.copy(
            analysisResult = null,
            showAnalysisResult = false,
            currentText = ""
        )
    }

    fun dismissError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun dismissAnalysisResult() {
        _uiState.value = _uiState.value.copy(showAnalysisResult = false)
    }

    fun loadRecentEntries() {
        viewModelScope.launch {
            val history = repository.getHistory()
            val streak = calculateStreak(history)
            _uiState.value = _uiState.value.copy(
                recentEntries = history,
                streak = streak
            )
        }
    }

    private fun calculateStreak(history: List<SentimentResult>): Int {
        if (history.isEmpty()) return 0
        val dateSet = history.map { it.timestamp / (24 * 60 * 60 * 1000L) }.toSet()
        var streak = 0
        var dayPointer = System.currentTimeMillis() / (24 * 60 * 60 * 1000L)
        while (dateSet.contains(dayPointer)) {
            streak++
            dayPointer--
        }
        return streak
    }

    // Quick analysis for common emotions - for enhanced user experience
    fun getEmotionInsight(text: String): String {
        val lowerText = text.lowercase()

        return when {
            lowerText.contains("happy") || lowerText.contains("joy") || lowerText.contains("excited") ||
                    lowerText.contains("great") || lowerText.contains("amazing") ->
                "It's wonderful to see positive emotions! 😊 Keep nurturing that positivity."

            lowerText.contains("sad") || lowerText.contains("depressed") || lowerText.contains("unhappy") ||
                    lowerText.contains("miserable") || lowerText.contains("hurt") ->
                "It's okay to feel down sometimes. Remember, this too shall pass. 💙"

            lowerText.contains("angry") || lowerText.contains("mad") || lowerText.contains("frustrated") ||
                    lowerText.contains("annoyed") || lowerText.contains("rage") ->
                "Anger is a natural emotion. Try taking deep breaths or going for a walk. 🌬️"

            lowerText.contains("scared") || lowerText.contains("anxious") || lowerText.contains("worried") ||
                    lowerText.contains("nervous") || lowerText.contains("stress") ->
                "Anxiety can be challenging. Focus on what you can control in this moment. 🌟"

            lowerText.contains("love") || lowerText.contains("care") || lowerText.contains("appreciate") ||
                    lowerText.contains("grateful") || lowerText.contains("thankful") ->
                "Love and appreciation are beautiful emotions! ❤️"

            lowerText.contains("tired") || lowerText.contains("exhausted") || lowerText.contains("sleep") ->
                "Rest is important for emotional well-being. Listen to your body. 😴"

            else -> "Thanks for sharing your thoughts. Reflection is the first step to understanding yourself better. 📝"
        }
    }

    // Get writing prompts based on current mood
    fun getWritingPrompt(): String {
        val prompts = listOf(
            "What made you smile today?",
            "What's something you're looking forward to?",
            "Describe a challenge you overcame recently.",
            "What are you grateful for right now?",
            "Write about a person who inspires you.",
            "What does your ideal day look like?",
            "Describe a recent learning experience.",
            "What helps you feel calm and centered?",
            "What would you tell your younger self?",
            "What's a small win you had this week?",
            "Describe a moment that made you feel proud.",
            "What's something you'd like to improve about yourself?"
        )
        return prompts.random()
    }

    // Clear all history
    fun clearAllHistory() {
        viewModelScope.launch {
            repository.clearHistory()
            loadRecentEntries()
        }
    }

    // Get sentiment statistics
    fun getSentimentStats(): Triple<Int, Int, Int> {
        val history = _uiState.value.recentEntries
        val positive = history.count { it.sentiment == SentimentType.POSITIVE }
        val negative = history.count { it.sentiment == SentimentType.NEGATIVE }
        val neutral = history.count { it.sentiment == SentimentType.NEUTRAL }
        return Triple(positive, negative, neutral)
    }
}