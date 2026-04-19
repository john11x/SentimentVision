package com.example.sentimentvision.data.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sentimentvision.data.model.AppointmentEntity

@Dao
interface AppointmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(appointment: AppointmentEntity)

    @Query("SELECT * FROM appointments ORDER BY scheduledFor ASC")
    suspend fun getAll(): List<AppointmentEntity>

    @Query("DELETE FROM appointments")
    suspend fun clearAll()
}
