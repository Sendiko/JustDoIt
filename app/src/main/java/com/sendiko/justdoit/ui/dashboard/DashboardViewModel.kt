package com.sendiko.justdoit.ui.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*
import com.sendiko.justdoit.model.Task2
import com.sendiko.justdoit.ui.home.TaskAdapter
import kotlinx.coroutines.launch

class DashboardViewModel : ViewModel() {

   private lateinit var db : DatabaseReference

   var emptyList = false

   private val _isFailed = MutableLiveData<Boolean>()
   val isFailed : LiveData<Boolean> = _isFailed

   private val _isLoading = MutableLiveData<Boolean>()
   val isLoading : LiveData<Boolean> = _isLoading

   private val _isEmpty = MutableLiveData<Boolean>()
   val isEmpty : LiveData<Boolean> = _isEmpty

   fun getTaskData(taskArrayList: ArrayList<Task2>, recyclerView: RecyclerView){
      _isLoading.value = true
      viewModelScope.launch {
         db = FirebaseDatabase.getInstance().getReference("this_checked")
         db.addValueEventListener(
            object : ValueEventListener {
               override fun onDataChange(snapshot: DataSnapshot) {
                  taskArrayList.clear()
                  when{
                     snapshot.exists() -> {
                        for (s in snapshot.children){
                           val task = s.getValue(Task2::class.java)
                           taskArrayList.add(task!!)
                           _isLoading.value = false
                           _isEmpty.value = false
                           emptyList = false
                        }
                        recyclerView.adapter = Task2Adapter(taskArrayList)
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