package me.nemiron.khinkalyator

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import me.nemiron.khinkalyator.core.ComponentFactory
import me.nemiron.khinkalyator.core.KoinProvider
import org.koin.android.ext.koin.androidContext
import org.koin.core.Koin
import org.koin.dsl.koinApplication
import timber.log.Timber

class KhinkalyatorApp : Application(), KoinProvider {

    override lateinit var koin: Koin
        private set

    override fun onCreate() {
        super.onCreate()
        koin = createKoin()
        koin.declare(ComponentFactory(koin))

        // TODO: dark theme
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        initLogger()
    }

    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun createKoin(): Koin {
        return koinApplication {
            androidContext(this@KhinkalyatorApp)
            modules(allModules)
        }.koin
    }
}