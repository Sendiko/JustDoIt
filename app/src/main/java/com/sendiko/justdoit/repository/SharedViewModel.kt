package com.sendiko.justdoit.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sendiko.justdoit.repository.model.Task

class SharedViewModel : ViewModel() {

   private val _task = MutableLiveData<Task>()
   val task : LiveData<Task> = _task

   fun saveTask(task: Task){
      _task.value = task
   }

}