package com.example.sentimentvision.data.repository

import com.example.sentimentvision.data.model.SettingsEntity

class SettingsRepository(private val settingsDao: SettingsDao) {

    suspend fun getSettings(): SettingsEntity {
        return settingsDao.get() ?: SettingsEntity().also { settingsDao.upsert(it) }
    }

    suspend fun updateSettings(settings: SettingsEntity) {
        settingsDao.upsert(settings)
    }
}
