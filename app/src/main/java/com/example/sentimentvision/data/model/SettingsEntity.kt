package com.example.sentimentvision.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class SettingsEntity(
    @PrimaryKey val id: Int = 0, // single row
    val darkMode: Boolean = false,
    val syncEnabled: Boolean = false,
    val remindersEnabled: Boolean = false
)
