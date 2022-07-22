package com.sendiko.justdoit.repository

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sendiko.justdoit.ui.dashboard.DashboardViewModel
import com.sendiko.justdoit.ui.home.HomeViewModel

class ViewModelFactory private constructor(private val application : Application) : ViewModelProvider.NewInstanceFactory(){
   companion object {
      @Volatile
      private var INSTANCE: ViewModelFactory? = null

      @JvmStatic
      fun getInstance(application: Application): ViewModelFactory {
         when (INSTANCE) {
            null -> {
               synchronized(ViewModelFactory::class.java) {
                  INSTANCE = ViewModelFactory(application)
               }
            }
         }
         return INSTANCE as ViewModelFactory
      }
   }

   override fun <T : ViewModel> create(modelClass: Class<T>): T {
      return when {
         modelClass.isAssignableFrom(HomeViewModel::class.java)-> HomeViewModel(application) as T
         else -> throw IllegalArgumentException("Unknown model class : " + modelClass.name)
      }
   }

}