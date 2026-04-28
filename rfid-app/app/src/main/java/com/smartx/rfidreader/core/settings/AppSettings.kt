package com.smartx.rfidreader.core.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

data class AppSettings(
    val lastReaderId: String = "",
    val buzzerEnabled: Boolean = true,
    val rssiFilter: Int = -120,
    val prefixes: List<String> = emptyList(),
    val webhookUrl: String = "",
    /** Último endereço MAC BLE selecionado (global) */
    val lastBleAddress: String = ""
)

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_settings")

class AppSettingsRepository(context: Context) {

    private val store = context.applicationContext.dataStore

    companion object {
        private val KEY_READER_ID = stringPreferencesKey("last_reader_id")
        private val KEY_BUZZER = booleanPreferencesKey("buzzer_enabled")
        private val KEY_RSSI = intPreferencesKey("rssi_filter")
        private val KEY_PREFIXES = stringPreferencesKey("prefixes")
        private val KEY_LAST_BLE = stringPreferencesKey("last_ble_address")
        private val KEY_WEBHOOK = stringPreferencesKey("webhook_url")
    }

    val flow: Flow<AppSettings> = store.data
        .catch { e ->
            if (e is IOException) emit(emptyPreferences()) else throw e
        }
        .map { prefs ->
            val prefixStr = prefs[KEY_PREFIXES] ?: ""
            AppSettings(
                lastReaderId = prefs[KEY_READER_ID] ?: "",
                buzzerEnabled = prefs[KEY_BUZZER] ?: true,
                rssiFilter = prefs[KEY_RSSI] ?: -120,
                prefixes = if (prefixStr.isBlank()) emptyList()
                           else prefixStr.split("|").filter { it.isNotBlank() },
                webhookUrl = prefs[KEY_WEBHOOK] ?: "",
                lastBleAddress = prefs[KEY_LAST_BLE] ?: ""
            )
        }

    suspend fun save(settings: AppSettings) {
        store.edit { prefs ->
            prefs[KEY_READER_ID] = settings.lastReaderId
            prefs[KEY_BUZZER] = settings.buzzerEnabled
            prefs[KEY_RSSI] = settings.rssiFilter
            prefs[KEY_PREFIXES] = settings.prefixes.joinToString("|")
            prefs[KEY_WEBHOOK] = settings.webhookUrl
            prefs[KEY_LAST_BLE] = settings.lastBleAddress
        }
    }
}
