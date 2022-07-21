package com.sendiko.justdoit.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.sendiko.justdoit.model.Task
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

   private lateinit var db : DatabaseReference

   private val _isFailed = MutableLiveData<Boolean>()
   val isFailed : LiveData<Boolean> = _isFailed

   private val _isLoading = MutableLiveData<Boolean>()
   val isLoading : LiveData<Boolean> = _isLoading

   private val _isEmpty = MutableLiveData<Boolean>()
   val isEmpty : LiveData<Boolean> = _isEmpty

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
                        }
                        recyclerView.adapter = TaskAdapter(taskArrayList)
                     }
                     else -> {
                        _isLoading.value = false
                        _isEmpty.value = true
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