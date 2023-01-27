package com.sendiko.justdoit.repository.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsPreference private constructor(private val dataStore: DataStore<Preferences>) {

    private val isDarkThemeKey = booleanPreferencesKey("isDarkTheme")
    private val languageKey = stringPreferencesKey("language")
    private val sortListKey = stringPreferencesKey("sort_list")

    suspend fun setSortListKey(sortKey: String) {
        dataStore.edit { key ->
            key[sortListKey] = sortKey
        }
    }

    fun getSortListKey(): Flow<String> {
        return dataStore.data.map { key ->
            key[sortListKey] ?: "none"
        }
    }

    suspend fun setDarkTheme(darkTheme: Boolean) {
        dataStore.edit { key ->
            key[isDarkThemeKey] = darkTheme
        }
    }

    fun getDarkTheme(): Flow<Boolean> {
        return dataStore.data.map { key ->
            key[isDarkThemeKey] ?: false
        }
    }

    suspend fun setLanguageKey(language: String) {
        dataStore.edit { key ->
            key[languageKey] = language
        }
    }

    fun getLanguageKey(): Flow<String> {
        return dataStore.data.map { key ->
            key[languageKey] ?: "en"
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SettingsPreference? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingsPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = SettingsPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

}