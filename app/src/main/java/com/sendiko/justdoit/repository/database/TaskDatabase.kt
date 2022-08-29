package com.sendiko.justdoit.repository.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sendiko.justdoit.repository.model.StringConstants
import com.sendiko.justdoit.repository.model.Task

@Database(entities = [Task::class], version = 2)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao() : TaskDataAccessObject

    companion object {
        @Volatile
        private var INSTANCE : TaskDatabase ?= null

        fun getDatabase(context: Context) : TaskDatabase {
            return INSTANCE ?: synchronized(this){
                 val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    StringConstants.dbName
                ).build()
                INSTANCE = instance
                instance
            }
        }

    }

}