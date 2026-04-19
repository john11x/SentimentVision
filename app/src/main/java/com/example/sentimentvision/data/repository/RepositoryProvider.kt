package com.example.sentimentvision.data.repository

import android.content.Context
import com.example.sentimentvision.domain.SentimentAnalyzer

object RepositoryProvider {
    private lateinit var sentimentRepo: SentimentRepository
    private lateinit var authRepo: AuthRepository
    private lateinit var settingsRepo: SettingsRepository

    fun init(context: Context) {
        val db = SentimentDatabase.getInstance(context)
        val sentimentRepoImpl = SentimentRepositoryRoomImpl(
            db.sentimentDao(),
            db.appointmentDao(),
            SentimentAnalyzer(),
            db.userDao(),
            db.settingsDao()
        )

        sentimentRepo = sentimentRepoImpl
        authRepo = AuthRepository(db.userDao())
        settingsRepo = SettingsRepository(db.settingsDao())
    }

    val sentimentRepository: SentimentRepository
        get() {
            check(::sentimentRepo.isInitialized) { "RepositoryProvider not initialized" }
            return sentimentRepo
        }

    val authRepository: AuthRepository
        get() {
            check(::authRepo.isInitialized) { "RepositoryProvider not initialized" }
            return authRepo
        }

    val settingsRepository: SettingsRepository
        get() {
            check(::settingsRepo.isInitialized) { "RepositoryProvider not initialized" }
            return settingsRepo
        }
}