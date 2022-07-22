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
      var success = true
      db = FirebaseDatabase.getInstance().getReference("this")
      viewModelScope.launch {
         val key = db.push().key.toString()
         val task = Task(key, t, s, false)
         db.child(key).setValue(task).addOnCompleteListener{
            success = true
         }.addOnFailureListener {
            success = false
         }
      }
      return success
   }

}