package com.smartx.xscan

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.smartx.rfid.manager.ReaderManager
import com.smartx.rfid.manager.ReaderRegistry
import com.smartx.xscan.core.db.AppDatabase
import com.smartx.xscan.core.events.EventRepository
import com.smartx.xscan.core.settings.AppSettingsRepository

class RfidApplication : Application() {

    val database: AppDatabase by lazy { AppDatabase.getInstance(this) }
    val eventRepository: EventRepository by lazy { EventRepository(database.eventDao()) }
    val settingsRepository: AppSettingsRepository by lazy { AppSettingsRepository(this) }

    /**
     * Gerenciador de leitores (biblioteca smartx-rfid), compartilhado por telas e serviços.
     * A lista de leitores vem diretamente do ReaderRegistry da versão da lib configurada no Gradle.
     */
    val readerManager: ReaderManager by lazy {
        ReaderManager(this, ReaderRegistry.createAll())
    }

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}
