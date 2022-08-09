package com.sendiko.justdoit.repository.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sendiko.justdoit.repository.model.Task

@Dao
interface TaskDataAccessObject {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM taskTable ORDER BY id ASC")
    fun getTask() : LiveData<List<Task>>

}