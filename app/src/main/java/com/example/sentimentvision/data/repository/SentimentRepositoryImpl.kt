package com.example.sentimentvision.data.repository

import com.example.sentimentvision.data.model.*
import com.example.sentimentvision.domain.SentimentAnalyzer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class SentimentRepositoryRoomImpl(
    private val sentimentDao: SentimentDao,
    private val appointmentDao: AppointmentDao,
    private val analyzer: SentimentAnalyzer,
    private val userDao: UserDao,
    private val settingsDao: SettingsDao
) : SentimentRepository {

    override suspend fun analyzeSentiment(text: String): Result<SentimentResult> = withContext(Dispatchers.IO) {
        return@withContext try {
            val result = analyzer.analyze(text)
            val entity = SentimentEntity(
                text = result.text,
                sentiment = result.sentiment.name,
                confidence = result.confidence,
                timestamp = result.timestamp
                // userId will default to 0 for now
            )
            sentimentDao.insert(entity)
            Result.success(result)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getHistory(): List<SentimentResult> = withContext(Dispatchers.IO) {
        val entities = sentimentDao.getAll()
        return@withContext entities.map { entity ->
            SentimentResult(
                text = entity.text,
                sentiment = SentimentType.valueOf(entity.sentiment),
                confidence = entity.confidence,
                timestamp = entity.timestamp
            )
        }
    }

    override suspend fun clearHistory() = withContext(Dispatchers.IO) {
        sentimentDao.clearAll()
    }

    override suspend fun saveResult(result: SentimentResult) = withContext(Dispatchers.IO) {
        val entity = SentimentEntity(
            text = result.text,
            sentiment = result.sentiment.name,
            confidence = result.confidence,
            timestamp = result.timestamp
        )
        sentimentDao.insert(entity)
    }

    // Add method to save sentiment with userId
    override suspend fun saveUserSentiment(userId: Int, text: String, sentiment: SentimentResult) = withContext(Dispatchers.IO) {
        val entity = SentimentEntity(
            text = text,
            sentiment = sentiment.sentiment.name,
            confidence = sentiment.confidence,
            timestamp = sentiment.timestamp,
            userId = userId
        )
        sentimentDao.insert(entity)
    }

    // Appointments
    override suspend fun scheduleAppointment(userName: String, scheduledFor: Long, note: String?) = withContext(Dispatchers.IO) {
        appointmentDao.insert(AppointmentEntity(userName = userName, scheduledFor = scheduledFor, note = note))
    }

    override suspend fun getAppointments() = withContext(Dispatchers.IO) {
        appointmentDao.getAll()
    }

    override suspend fun clearAppointments() = withContext(Dispatchers.IO) {
        appointmentDao.clearAll()
    }

    // Users (simple helpers)
    override fun getSentimentsByUser(userId: Int): Flow<List<SentimentEntity>> = sentimentDao.getByUserId(userId)

    // Settings
    suspend fun getSettings(): SettingsEntity = withContext(Dispatchers.IO) {
        settingsDao.get() ?: SettingsEntity().also { settingsDao.upsert(it) }
    }

    suspend fun updateSettings(settings: SettingsEntity) = withContext(Dispatchers.IO) {
        settingsDao.upsert(settings)
    }
}