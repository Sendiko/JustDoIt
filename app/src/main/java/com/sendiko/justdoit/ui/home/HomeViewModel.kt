package com.sendiko.justdoit.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.sendiko.justdoit.model.Task
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

   private lateinit var db : DatabaseReference

   private val context by lazy { getApplication<Application>().applicationContext }

   var emptyList = false

   private val _isFailed = MutableLiveData<Boolean>()
   val isFailed : LiveData<Boolean> = _isFailed

   private val _isLoading = MutableLiveData<Boolean>()
   val isLoading : LiveData<Boolean> = _isLoading

   private val _isEmpty = MutableLiveData<Boolean>()
   val isEmpty : LiveData<Boolean> = _isEmpty

   fun inputTask(t : String, s : String) : LiveData<Boolean> {
      val isDone = MutableLiveData<Boolean>()
      _isLoading.value = true
      viewModelScope.launch {
         isDone.value = false
         db = FirebaseDatabase.getInstance().getReference("this")
         val key = db.push().key.toString()
         val task = Task(key, t, s, false)
         db.child(key).setValue(task).addOnCompleteListener {
            _isLoading.value = false
            isDone.value = true
         }
      }
      return isDone
   }

   fun getTaskData(taskArrayList : ArrayList<Task>, recyclerView: RecyclerView){
      _isLoading.value = true
      viewModelScope.launch {
         db = FirebaseDatabase.getInstance().getReference("this")
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
                  _isFailed.value = true
               }

            }
         )
      }
   }

}