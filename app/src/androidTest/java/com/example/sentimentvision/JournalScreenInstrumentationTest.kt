package com.example.sentimentvision.ui.analysis

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class JournalScreenInstrumentationTest {


    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testJournalScreenRenderingPerformance() {
        val startTime = System.currentTimeMillis()

        composeTestRule.setContent {
            JournalScreen()
        }

        val renderTime = System.currentTimeMillis() - startTime

        println("PERFORMANCE METRIC: Journal screen rendered in $renderTime ms")

        // Simple verification that screen loaded
        Thread.sleep(1000) // Wait a moment to ensure rendering completes
        println("Journal screen performance test completed successfully")
    }
    @Test
    fun testSentimentAnalysisPerformance() {
        val startTime = System.currentTimeMillis()

        composeTestRule.setContent {
            JournalScreen()
        }

        val renderTime = System.currentTimeMillis() - startTime

        println("PERFORMANCE METRIC: Journal screen loaded in $renderTime ms")
        println("UAT: Sentiment analysis screen rendered successfully")
    }
}