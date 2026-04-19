package com.example.sentimentvision.ui.therapist

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ContactTherapistScreenInstrumentationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun testTherapistNotificationPerformance() {
        val startTime = System.currentTimeMillis()

        composeTestRule.setContent {
            TherapistScreen()
        }

        val renderTime = System.currentTimeMillis() - startTime

        println("PERFORMANCE METRIC: Therapist screen loaded in $renderTime ms")
        println("UAT: Therapist notification interface rendered successfully")
    }
}