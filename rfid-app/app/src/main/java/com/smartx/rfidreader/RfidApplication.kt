package com.smartx.rfidreader

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.smartx.rfidreader.core.db.AppDatabase
import com.smartx.rfidreader.core.events.EventRepository
import com.smartx.rfidreader.core.settings.AppSettingsRepository

class RfidApplication : Application() {

    val database: AppDatabase by lazy { AppDatabase.getInstance(this) }
    val eventRepository: EventRepository by lazy { EventRepository(database.eventDao()) }
    val settingsRepository: AppSettingsRepository by lazy { AppSettingsRepository(this) }

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}
