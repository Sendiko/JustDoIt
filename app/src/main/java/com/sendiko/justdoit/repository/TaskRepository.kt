package com.sendiko.justdoit.repository

import androidx.lifecycle.LiveData
import com.sendiko.justdoit.repository.database.TaskDataAccessObject
import com.sendiko.justdoit.repository.model.Task

class TaskRepository(private val taskDao : TaskDataAccessObject) {

    val allTask : LiveData<List<Task>> = taskDao.getTask()

    suspend fun insertTask(task: Task) = taskDao.insertTask(task)

    suspend fun updateTask(task: Task) = taskDao.updateTask(task)

    suspend fun deleteTask(task: Task) = taskDao.deleteTask(task)

}