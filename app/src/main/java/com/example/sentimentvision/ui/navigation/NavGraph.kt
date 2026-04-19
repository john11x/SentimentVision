package com.example.sentimentvision.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sentimentvision.ui.auth.LoginScreen
import com.example.sentimentvision.ui.components.SplashScreen
import com.example.sentimentvision.ui.dashboard.DashboardScreen
import com.example.sentimentvision.ui.analysis.JournalScreen
import com.example.sentimentvision.ui.analytics.EnhancedAnalyticsScreen
import com.example.sentimentvision.ui.therapist.ContactTherapistScreen
import com.example.sentimentvision.ui.therapist.TherapistDashboardActivity
import com.example.sentimentvision.ui.games.BreathingExerciseScreen
import com.example.sentimentvision.ui.games.PositiveReframeScreen
import kotlinx.coroutines.delay
import androidx.compose.ui.platform.LocalContext
import android.content.Intent
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.ConnectionResult

private fun checkGooglePlayServicesAvailability(context: android.content.Context): Boolean {
    return try {
        val googleApiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context)
        resultCode == ConnectionResult.SUCCESS
    } catch (e: Exception) {
        false
    }
}

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Dashboard : Screen("dashboard")
    object Journal : Screen("journal")
    object Therapist : Screen("therapist")
    object ContactTherapist : Screen("contact_therapist")
    object EnhancedAnalytics : Screen("enhanced_analytics")
    object BreathingExercise : Screen("breathing_exercise")
    object PositiveReframe : Screen("positive_reframe")
}

@Composable
fun NavGraph() {
    val navController = rememberNavController()
    var showSplash by remember { mutableStateOf(true) }
    val context = LocalContext.current
    val isGooglePlayServicesAvailable = remember {
        checkGooglePlayServicesAvailability(context)
    }

    LaunchedEffect(Unit) {
        delay(1400)
        showSplash = false
    }

    if (showSplash) {
        SplashScreen()
    } else {
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route
        ) {
            composable(Screen.Login.route) {
                LoginScreen(
                    onLogin = {
                        navController.navigate(Screen.Dashboard.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    onContactTherapist = {
                        navController.navigate(Screen.ContactTherapist.route)
                    }
                )
            }

            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    onOpenJournal = { navController.navigate(Screen.Journal.route) },
                    onOpenTherapist = {
                        context.startActivity(Intent(context, TherapistDashboardActivity::class.java))
                    },
                    onOpenAnalytics = { navController.navigate(Screen.EnhancedAnalytics.route) }
                )
            }

            composable(Screen.Journal.route) {
                JournalScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.ContactTherapist.route) {
                ContactTherapistScreen()
            }

            composable(Screen.EnhancedAnalytics.route) {
                EnhancedAnalyticsScreen(
                    onBack = { navController.popBackStack() },
                    onNavigateToBreathingExercise = {
                        navController.navigate(Screen.BreathingExercise.route)
                    },
                    onNavigateToPositiveReframe = {
                        navController.navigate(Screen.PositiveReframe.route)
                    }
                )
            }

            composable(Screen.BreathingExercise.route) {
                BreathingExerciseScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.PositiveReframe.route) {
                PositiveReframeScreen(
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}