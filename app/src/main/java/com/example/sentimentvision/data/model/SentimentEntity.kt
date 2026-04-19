package com.example.sentimentvision.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sentiment_results")
data class SentimentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String,
    val sentiment: String,
    val confidence: Float,
    val timestamp: Long,
    val userId: Int = 0
)