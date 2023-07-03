package com.github.kitakkun.noteapp.data.store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

private object SettingKey {
    val AUTO_SAVE_ENABLED = booleanPreferencesKey("auto_save_enabled")
    val SHOW_LINE_NUMBER = booleanPreferencesKey("show_line_number")
}

class SettingDataStore(
    private val context: Context,
) {
    private val Context.datastore: DataStore<Preferences> by preferencesDataStore(name = "setting")

    val autoSaveEnabledFlow = context.datastore.data.map {
        it[SettingKey.AUTO_SAVE_ENABLED] ?: false
    }

    val showLineNumberFlow = context.datastore.data.map {
        it[SettingKey.SHOW_LINE_NUMBER] ?: false
    }

    suspend fun setAutoSaveEnabled(isEnabled: Boolean) {
        context.datastore.edit { it[SettingKey.AUTO_SAVE_ENABLED] = isEnabled }
    }

    suspend fun setShowLineNumber(isEnabled: Boolean) {
        context.datastore.edit { it[SettingKey.SHOW_LINE_NUMBER] = isEnabled }
    }
}
