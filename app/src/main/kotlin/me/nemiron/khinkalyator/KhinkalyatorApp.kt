package me.nemiron.khinkalyator

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import timber.log.Timber

class KhinkalyatorApp : Application() {

    override fun onCreate() {
        super.onCreate()
        // TODO: dark theme
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        initLogger()
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}