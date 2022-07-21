package com.sendiko.justdoit.ui.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.sendiko.justdoit.model.Task
import kotlinx.coroutines.launch

class TaskViewModel : ViewModel() {

   private lateinit var db : DatabaseReference

   fun insertTask(t : String, s : String) : Boolean {
      var success = false
      val task = Task(t, s, false)
      viewModelScope.launch {
         db = FirebaseDatabase.getInstance().getReference("this")
         val key = db.push().key.toString()
         db.child(key).setValue(task).addOnCompleteListener{
            success = true
         }
      }
      return success
   }

}