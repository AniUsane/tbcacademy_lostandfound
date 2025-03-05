package com.example.lostfound

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.util.Log
import android.util.Log.d
import android.util.Log.e
import com.google.firebase.FirebaseApp
import com.google.firebase.installations.FirebaseInstallations
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.Locale

@HiltAndroidApp
class App: Application() {
    override fun onCreate() {
        super.onCreate()

        //initializes firebase
        FirebaseApp.initializeApp(this)
        applyLanguage()
    }

    //applies the saved language
    private fun applyLanguage(){
        val sharedPreferences: SharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val savedLanguage = sharedPreferences.getString("language", "en") ?: "en"

        val locale = Locale(savedLanguage)
        Locale.setDefault(locale)

        val config = Configuration(resources.configuration)
        config.setLocale(locale)

        baseContext.createConfigurationContext(config)
    }
}