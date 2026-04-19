package com.example.sentimentvision.data.repository

import com.example.sentimentvision.data.model.AppointmentEntity
import com.example.sentimentvision.data.model.SentimentEntity
import com.example.sentimentvision.data.model.SentimentResult
import kotlinx.coroutines.flow.Flow

interface SentimentRepository {
    suspend fun analyzeSentiment(text: String): Result<SentimentResult>
    suspend fun getHistory(): List<SentimentResult>
    suspend fun clearHistory()
    suspend fun saveResult(result: SentimentResult)

    suspend fun saveUserSentiment(userId: Int, text: String, sentiment: SentimentResult)
    fun getSentimentsByUser(userId: Int): Flow<List<SentimentEntity>>


    suspend fun scheduleAppointment(userName: String, scheduledFor: Long, note: String?)
    suspend fun getAppointments(): List<AppointmentEntity>
    suspend fun clearAppointments()
}