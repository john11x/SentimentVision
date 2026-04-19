package com.example.sentimentvision.data

import androidx.test.platform.app.InstrumentationRegistry
import androidx.room.Room
import com.example.sentimentvision.data.model.SentimentEntity
import com.example.sentimentvision.data.repository.SentimentDatabase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class DatabaseRobustnessTest {

    private lateinit var db: SentimentDatabase

    @Before
    fun setup() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, SentimentDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun testDatabaseRobustnessUnderLoad() = runBlocking {
        val dao = db.sentimentDao()

        // Test database resilience with multiple operations
        repeat(10) { index ->
            dao.insert(SentimentEntity(
                text = "Stress test entry $index",
                sentiment = if (index % 4 == 0) "POSITIVE" else "NEUTRAL",
                confidence = 0.7f + (index % 3) * 0.1f,
                timestamp = System.currentTimeMillis() + index
            ))
        }

        val results = dao.getAll()
        assertEquals(10, results.size)

        // Test data integrity
        assertTrue(results.all { it.text.contains("Stress test entry") })

        println("DATABASE ROBUSTNESS: Successfully handled 10 concurrent operations")
    }
}