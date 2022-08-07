package com.sendiko.justdoit.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.sendiko.justdoit.repository.model.FailedMessage
import com.sendiko.justdoit.repository.model.Task
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

   private lateinit var db : DatabaseReference

   private lateinit var auth : FirebaseAuth

   private val context by lazy { getApplication<Application>().applicationContext }

   var emptyList = false

   private val _isFailed = MutableLiveData<FailedMessage>()
   val isFailed : LiveData<FailedMessage> = _isFailed

   private val _isLoading = MutableLiveData<Boolean>()
   val isLoading : LiveData<Boolean> = _isLoading

   private val _isEmpty = MutableLiveData<Boolean>()
   val isEmpty : LiveData<Boolean> = _isEmpty

   fun inputTask(t : String, s : String) : LiveData<Boolean> {
      val isDone = MutableLiveData<Boolean>()
      _isLoading.value = true
      viewModelScope.launch {
         isDone.value = false
         val key = db.push().key.toString()
         val task = Task(key, t, s, false)
         val user = auth.currentUser
         db = FirebaseDatabase.getInstance().getReference("${user?.uid}_this")
         db.child(key).setValue(task).addOnSuccessListener {
            _isLoading.value = false
            isDone.value = true
         }.addOnFailureListener {
            isDone.value = true
            _isLoading.value = false
            _isFailed.value = FailedMessage(true, it.message)
         }
      }
      return isDone
   }

   fun getTaskData(taskArrayList : ArrayList<Task>, recyclerView: RecyclerView){
      _isLoading.value = true
      auth = Firebase.auth
      viewModelScope.launch {
         val user = auth.currentUser
         db = FirebaseDatabase.getInstance().getReference("${user?.uid}_this")
         db.addValueEventListener(
            object : ValueEventListener {
               override fun onDataChange(snapshot: DataSnapshot) {
                  taskArrayList.clear()
                  when{
                     snapshot.exists() -> {
                        for (s in snapshot.children){
                           val task = s.getValue(Task::class.java)
                           taskArrayList.add(task!!)
                           _isLoading.value = false
                           _isEmpty.value = false
                           emptyList = false
                        }
                        recyclerView.adapter = TaskAdapter(taskArrayList,context)
                     }
                     else -> {
                        _isLoading.value = false
                        _isEmpty.value = true
                        emptyList = true
                     }
                  }
               }

               override fun onCancelled(error: DatabaseError) {
                  Log.d("onCancelled", error.message)
                  _isLoading.value = false
                  _isFailed.value = FailedMessage(true, error.message)
               }

            }
         )
      }
   }

}