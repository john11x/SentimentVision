package com.example.sentimentvision

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.sentimentvision.data.model.SentimentEntity
import com.example.sentimentvision.data.repository.SentimentDatabase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class RoomSeedTest {

    private lateinit var db: SentimentDatabase

    @Before
    fun setup() {
        val ctx = ApplicationProvider.getApplicationContext<android.content.Context>()
        db = Room.inMemoryDatabaseBuilder(ctx, SentimentDatabase::class.java).allowMainThreadQueries().build()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun seedAndRetrieve() = runBlocking {
        val dao = db.sentimentDao()
        val now = System.currentTimeMillis()
        dao.insert(SentimentEntity(text = "I am happy", sentiment = "POSITIVE", confidence = 0.9f, timestamp = now))
        dao.insert(SentimentEntity(text = "I feel sad", sentiment = "NEGATIVE", confidence = 0.8f, timestamp = now - 86400000L))
        val all = dao.getAll()
        assertEquals(2, all.size)
        assertTrue(all.any { it.text.contains("happy") })
    }
}
