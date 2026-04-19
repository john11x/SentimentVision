package com.example.sentimentvision.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "appointments")
data class AppointmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userName: String,
    val scheduledFor: Long,
    val note: String?,
    val createdAt: Long = System.currentTimeMillis()
)
