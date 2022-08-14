package com.sendiko.justdoit.repository

import androidx.lifecycle.LiveData
import com.sendiko.justdoit.repository.database.TaskDataAccessObject
import com.sendiko.justdoit.repository.model.Task

class TaskRepository(private val taskDao : TaskDataAccessObject) {

    val allTask : LiveData<List<Task>> = taskDao.getTask()

    val allCheckTask : LiveData<List<Task>> = taskDao.getCheckedTask()

    val checkIfEmpty : LiveData<Int> = taskDao.checkIfEmpty()

    val alsoCheckIfEmpty : LiveData<Int> = taskDao.alsoCheckIfEmpty()

    val sortAZ : LiveData<List<Task>> = taskDao.sortAZ()

    val sortZA : LiveData<List<Task>> = taskDao.sortZA()

    suspend fun insertTask(task: Task) = taskDao.insertTask(task)

    suspend fun updateTask(task: Task) = taskDao.updateTask(task)

    suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)

}