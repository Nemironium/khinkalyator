package me.nemiron.khinkalyator.core

import android.app.Application
import androidx.sqlite.db.SupportSQLiteDatabase
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import kotlinx.coroutines.Dispatchers
import me.nemiron.khinkalyator.KhinkalytorDatabase
import me.nemiron.khinkalyator.core.keyboard.CloseKeyboardService
import me.nemiron.khinkalyator.core.keyboard.CloseKeyboardServiceImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coreModule = module {
    singleOf(::CloseKeyboardServiceImpl) { bind<CloseKeyboardService>() }
    single {
        val driver = provideSqlDriver(androidApplication())
        KhinkalytorDatabase(driver)
    }
    // TODO: (when needed another Dispatcher) add named dependency
    single { Dispatchers.IO }
}

fun provideSqlDriver(app: Application): SqlDriver {
    return AndroidSqliteDriver(
        schema = KhinkalytorDatabase.Schema,
        context = app,
        name = "khinkalyator.db",
        callback = object : AndroidSqliteDriver.Callback(KhinkalytorDatabase.Schema) {
            override fun onOpen(db: SupportSQLiteDatabase) {
                db.setForeignKeyConstraintsEnabled(true)
                db.enableWriteAheadLogging()
            }
        }
    )
}