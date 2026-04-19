package com.example.sentimentvision.domain

import com.example.sentimentvision.data.model.SentimentResult
import com.example.sentimentvision.data.model.SentimentType
import kotlinx.coroutines.delay
class SentimentAnalyzer {

    suspend fun analyze(text: String): SentimentResult {
        // Simulate processing time
        delay(500)

        val sentiment = mockAnalysis(text)
        val confidence = (70..95).random() / 100f

        return SentimentResult(
            text = text,
            sentiment = sentiment,
            confidence = confidence
        )
    }

    private fun mockAnalysis(text: String): SentimentType {
        val lowerText = text.lowercase()

        val positiveWords = listOf("good", "great", "excellent", "amazing", "wonderful",
            "fantastic", "love", "happy", "best", "perfect")
        val negativeWords = listOf("bad", "terrible", "awful", "horrible", "worst",
            "hate", "sad", "poor", "disappointing", "useless")

        val positiveCount = positiveWords.count { lowerText.contains(it) }
        val negativeCount = negativeWords.count { lowerText.contains(it) }

        return when {
            positiveCount > negativeCount -> SentimentType.POSITIVE
            negativeCount > positiveCount -> SentimentType.NEGATIVE
            else -> SentimentType.NEUTRAL
        }
    }


}