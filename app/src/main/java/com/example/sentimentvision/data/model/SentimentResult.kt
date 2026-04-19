package com.example.sentimentvision.data.model

data class SentimentResult(
    val text: String,
    val sentiment: SentimentType,
    val confidence: Float,
    val timestamp: Long = System.currentTimeMillis()
)