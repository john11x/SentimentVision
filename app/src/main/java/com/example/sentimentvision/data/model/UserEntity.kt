package com.example.sentimentvision.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val pinHash: String,
    val displayName: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
