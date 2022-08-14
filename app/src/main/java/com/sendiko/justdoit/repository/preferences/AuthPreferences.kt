package com.sendiko.justdoit.repository.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AuthPreferences private constructor(private val dataStore: DataStore<Preferences>){

   private val loginKey = booleanPreferencesKey("login")
   private val usernameKey = stringPreferencesKey("username")

   suspend fun clearUsername(){
      dataStore.edit { key ->
         key.remove(usernameKey)
      }
   }

   fun getUsername() : Flow<String> {
      return dataStore.data.map { key ->
         key[usernameKey]?:""
      }
   }

   suspend fun saveUsername(username : String){
      dataStore.edit { key ->
         key[usernameKey] = username
      }
   }

   fun getLoginState() : Flow<Boolean> {
      return dataStore.data.map { key ->
         key[loginKey]?: false
      }
   }

   suspend fun setLoginState(isLoggedIn : Boolean){
      dataStore.edit { key ->
         key[loginKey] = isLoggedIn
      }
   }

   companion object{
      @Volatile
      private var INSTANCE : AuthPreferences ?= null

      fun getInstance(dataStore: DataStore<Preferences>) : AuthPreferences{
         return INSTANCE ?: kotlin.synchronized(this){
            val instance = AuthPreferences(dataStore)
            INSTANCE = instance
            instance
         }
      }
   }

}