package com.example.lostfound.presentation.profile

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import org.intellij.lang.annotations.Language
import java.util.Locale

object LocaleManager {
    private const val PREFS_NAME = "app_prefs"
    private const val LANGUAGE_KEY = "language"

    //sets the app locale and updates the configuration
    fun setLocale(context: Context, language: String): Context {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        sharedPreferences.edit().putString(LANGUAGE_KEY, language).apply()

        //sets new locale
        val locale = Locale(language)
        Locale.setDefault(locale)

        //updates app configuration with new locale
        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        return context.createConfigurationContext(config)
    }

    //gets the saved language preference from shared preferences
    fun getSavedLanguage(context: Context): String {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return sharedPreferences.getString(LANGUAGE_KEY, "en") ?: "en"
    }


}