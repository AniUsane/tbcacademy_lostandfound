package com.example.lostfound.presentation.base

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.example.lostfound.presentation.profile.LocaleManager

//using base class to change language globally
open class BaseActivity : AppCompatActivity() {
    override fun attachBaseContext(newBase: Context) {
        val language = LocaleManager.getSavedLanguage(newBase)
        val updatedContext = LocaleManager.setLocale(newBase, language)
        super.attachBaseContext(updatedContext)
    }
}