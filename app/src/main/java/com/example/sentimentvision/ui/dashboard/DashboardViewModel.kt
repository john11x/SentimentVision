package com.example.sentimentvision.ui.dashboard

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

data class DashboardUiState(
    val isAnalyzing: Boolean = false,
    val currentText: String = "",
    val currentResult: SentimentResult? = null,
    val history: List<SentimentResult> = emptyList(),
    val sentimentData: SentimentData = SentimentData(0, 0, 0),
    val error: String? = null
)

class DashboardViewModel : ViewModel() {

    private val repository = RepositoryProvider.sentimentRepository

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadHistory()
    }

    fun onTextChanged(text: String) {
        _uiState.value = _uiState.value.copy(currentText = text, error = null)
    }

    fun analyzeSentiment() {
        val text = _uiState.value.currentText.trim()

        if (text.isEmpty()) {
            _uiState.value = _uiState.value.copy(error = "Please enter some text to analyze")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isAnalyzing = true, error = null)

            repository.analyzeSentiment(text).fold(
                onSuccess = { result ->
                    _uiState.value = _uiState.value.copy(
                        isAnalyzing = false,
                        currentResult = result,
                        currentText = ""
                    )
                    loadHistory()
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isAnalyzing = false,
                        error = "Analysis failed: ${exception.message}"
                    )
                }
            )
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
            loadHistory()
            _uiState.value = _uiState.value.copy(currentResult = null)
        }
    }

    // ✅ Changed from private to public
    fun loadHistory() {
        viewModelScope.launch {
            val history = repository.getHistory()
            val sentimentData = calculateSentimentData(history)
            _uiState.value = _uiState.value.copy(
                history = history,
                sentimentData = sentimentData
            )
        }
    }

    private fun calculateSentimentData(history: List<SentimentResult>): SentimentData {
        val positive = history.count { it.sentiment == SentimentType.POSITIVE }
        val negative = history.count { it.sentiment == SentimentType.NEGATIVE }
        val neutral = history.count { it.sentiment == SentimentType.NEUTRAL }
        return SentimentData(positive, negative, neutral)
    }
}