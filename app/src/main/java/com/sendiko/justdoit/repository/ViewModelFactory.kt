package com.sendiko.justdoit.repository

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sendiko.justdoit.repository.helper.Constant
import com.sendiko.justdoit.ui.task.TaskViewModel

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
         modelClass.isAssignableFrom(TaskViewModel::class.java)-> TaskViewModel(application) as T
         else -> throw IllegalArgumentException(Constant.unknownModelClass + modelClass.name)
      }
   }

}