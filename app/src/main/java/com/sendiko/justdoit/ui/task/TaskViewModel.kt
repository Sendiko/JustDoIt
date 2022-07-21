package com.sendiko.justdoit.ui.task

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.sendiko.justdoit.model.Task

class TaskViewModel : ViewModel() {

   private lateinit var db : DatabaseReference

   private val _success = MutableLiveData<Boolean>()
   val success : LiveData<Boolean> = _success

   private val _isLoading = MutableLiveData<Boolean>()
   val isLoading : LiveData<Boolean> = _success

   fun insertTask(t : String, s : String){
      _isLoading.value = true
      db = FirebaseDatabase.getInstance().getReference("this")
      val task = Task(t, s, false)
      val key = db.push().key.toString()
      db.child(key).setValue(task).addOnCompleteListener{
         _isLoading.value = false
         _success.value = true
      }
   }

}