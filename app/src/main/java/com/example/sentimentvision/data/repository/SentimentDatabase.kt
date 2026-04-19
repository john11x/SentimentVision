package com.example.sentimentvision.data.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sentimentvision.data.model.*

@Database(
    entities = [SentimentEntity::class, AppointmentEntity::class, UserEntity::class, SettingsEntity::class],
    version = 2, //
    exportSchema = false
)
abstract class SentimentDatabase : RoomDatabase() {
    abstract fun sentimentDao(): SentimentDao
    abstract fun appointmentDao(): AppointmentDao
    abstract fun userDao(): UserDao
    abstract fun settingsDao(): SettingsDao

    companion object {
        @Volatile
        private var INSTANCE: SentimentDatabase? = null

        fun getInstance(context: Context): SentimentDatabase {
            return INSTANCE ?: synchronized(this) {
                val db = Room.databaseBuilder(
                    context.applicationContext,
                    SentimentDatabase::class.java,
                    "sentiment_db"
                ).fallbackToDestructiveMigration()
                    .build()
                INSTANCE = db
                db
            }
        }
    }
}