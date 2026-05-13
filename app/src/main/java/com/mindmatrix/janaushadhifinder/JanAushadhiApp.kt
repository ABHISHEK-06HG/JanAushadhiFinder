package com.mindmatrix.janaushadhifinder

import android.app.Application
import org.osmdroid.config.Configuration

class JanAushadhiApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize OSMDroid configuration
        Configuration.getInstance().load(this, getSharedPreferences("osmdroid", MODE_PRIVATE))
    }
}
