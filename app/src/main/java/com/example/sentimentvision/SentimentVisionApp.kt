package com.example.sentimentvision

import android.app.Application
import com.example.sentimentvision.data.repository.RepositoryProvider

class SentimentVisionApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // initialize repository provider (Room database created here)
        RepositoryProvider.init(applicationContext)
    }
}
