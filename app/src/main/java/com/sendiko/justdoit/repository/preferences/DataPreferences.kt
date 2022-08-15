package com.sendiko.justdoit.repository.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataPreferences private constructor(private val dataStore : DataStore<Preferences>){

    private val sortKey = stringPreferencesKey("sort")

    suspend fun setSortKey(sort : String){
        dataStore.edit { key ->
            key[sortKey] = sort
        }
    }

    fun getSortKey() : Flow<String> {
        return dataStore.data.map { key ->
            key[sortKey]?:""
        }
    }

    companion object{
        @Volatile
        private var INSTANCE : DataPreferences ?= null

        fun getInstance(dataStore: DataStore<Preferences>) : DataPreferences{
            return INSTANCE ?: kotlin.synchronized(this){
                val instance = DataPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }

}