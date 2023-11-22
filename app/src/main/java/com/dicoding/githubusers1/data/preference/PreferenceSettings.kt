package com.dicoding.githubusers1.data.preference

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.prefDataStore by preferencesDataStore("settings")

class PreferenceSettings constructor(context: Context) {

    private val dataStoreForSettings = context.prefDataStore
    private val temaKEY = booleanPreferencesKey("theme_setting")

    fun getSettingsTheme(): Flow<Boolean> =
        dataStoreForSettings.data.map { preferences ->
            preferences[temaKEY] ?: false
        }

    suspend fun saveSettingsTheme(isDarkModeActive: Boolean) {
        dataStoreForSettings.edit { preferences ->
            preferences[temaKEY] = isDarkModeActive
        }
    }
}