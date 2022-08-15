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

    @Query("select * from taskTable where isDone = 'false' order by task asc")
    fun getTask() : LiveData<List<Task>>

    @Query("select * from taskTable where isDone = 'true' order by task asc")
    fun getCheckedTask() : LiveData<List<Task>>

    @Query("select count (*) from taskTable as tableTask where isDone = 'false' ")
    fun checkIfEmpty() : LiveData<Int>

    @Query("select count (*) from taskTable as tableTask where isDone = 'true' ")
    fun alsoCheckIfEmpty() : LiveData<Int>

}