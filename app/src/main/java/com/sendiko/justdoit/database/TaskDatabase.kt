package com.sendiko.justdoit.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sendiko.justdoit.model.Task

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        var INSTANCE : TaskDatabase?= null
        fun getInstance(context: Context): TaskDatabase {
            when{
                INSTANCE == null -> {
                    INSTANCE =
                        Room.databaseBuilder(
                            context,
                            TaskDatabase::class.java,
                            Constants.databaseName
                        ).build()
                }
            }
            return INSTANCE as TaskDatabase
        }
    }

}