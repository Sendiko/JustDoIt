package com.sendiko.justdoit.repository.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsPreference private constructor(private val dataStore : DataStore<Preferences>){

    private val isDarkThemeKey = booleanPreferencesKey("isDarkTheme")
    private val languageKey = stringPreferencesKey("language")

    suspend fun setDarkTheme(darkTheme : Boolean){
        dataStore.edit { key ->
            key[isDarkThemeKey] = darkTheme
        }
    }

    fun getDarkTheme() : Flow<Boolean> {
        return dataStore.data.map { key ->
            key[isDarkThemeKey]?: false
        }
    }

    suspend fun setLanguageKey(languange : String){
        dataStore.edit { key ->
            key[languageKey] = languange
        }
    }

    fun getLanguageKey() : Flow<String> {
        return dataStore.data.map { key ->
            key[languageKey]?: "en"
        }
    }

    companion object{
        @Volatile
        private var INSTANCE : SettingsPreference ?= null

        fun getInstance(dataStore: DataStore<Preferences>) : SettingsPreference{
            return INSTANCE ?: kotlin.synchronized(this){
                val instance = SettingsPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

}