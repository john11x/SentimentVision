package com.example.sentimentvision.data.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sentimentvision.data.model.SentimentEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SentimentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(result: SentimentEntity)

    @Query("SELECT * FROM sentiment_results ORDER BY timestamp DESC")
    suspend fun getAll(): List<SentimentEntity>

    @Query("SELECT * FROM sentiment_results WHERE userId = :userId ORDER BY timestamp DESC")
    fun getByUserId(userId: Int): Flow<List<SentimentEntity>>

    @Query("DELETE FROM sentiment_results")
    suspend fun clearAll()
}