package com.example.sentimentvision.utils

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily

/**
 * Safe font family provider that handles Google Fonts provider crashes
 */
@Composable
fun safeFontFamily(): FontFamily {
    val context = LocalContext.current
    return if (isGooglePlayServicesAvailable(context)) {
        // Use default fonts which might rely on Google Fonts provider
        FontFamily.Default
    } else {
        // Fallback to system fonts
        FontFamily.SansSerif
    }
}

fun isGooglePlayServicesAvailable(context: Context): Boolean {
    return try {
        val googleApiAvailability = com.google.android.gms.common.GoogleApiAvailability.getInstance()
        val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(context)
        resultCode == com.google.android.gms.common.ConnectionResult.SUCCESS
    } catch (e: Exception) {
        false
    }
}