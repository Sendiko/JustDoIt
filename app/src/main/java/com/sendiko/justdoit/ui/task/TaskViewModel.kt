package com.sendiko.justdoit.ui.task

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.sendiko.justdoit.repository.TaskRepository
import com.sendiko.justdoit.repository.database.TaskDatabase
import com.sendiko.justdoit.repository.model.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = TaskDatabase.getDatabase(application).taskDao()
    private val repo : TaskRepository = TaskRepository(dao)
    val allTasks : LiveData<List<Task>> = repo.allTask
    val allCheckTasks : LiveData<List<Task>> = repo.allCheckTask
    val checkIfEmpty : LiveData<Int> = repo.checkIfEmpty
    val alsoCheckIfEmpty : LiveData<Int> = repo.alsoCheckIfEmpty

    fun insertTask(task: Task) = viewModelScope.launch(Dispatchers.IO){
        repo.insertTask(task)
    }

    fun updateTask(task: Task) = viewModelScope.launch(Dispatchers.IO){
        repo.updateTask(task)
    }

    fun deleteTask(task: Task) = viewModelScope.launch(Dispatchers.IO){
        repo.deleteTask(task)
    }

}