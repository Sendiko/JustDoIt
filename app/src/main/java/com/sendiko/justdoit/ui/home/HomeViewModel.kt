package com.sendiko.justdoit.ui.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.sendiko.justdoit.model.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : ViewModel() {

   private lateinit var db : DatabaseReference

   private var _isExist = MutableLiveData<Boolean>()
   val isExist : LiveData<Boolean> = _isExist

   private var _failedMessage = MutableLiveData<String>()
   val failedMessage : LiveData<String> = _failedMessage

   fun getTask(arraylist : ArrayList<Task>, recyclerView: RecyclerView){
      db = FirebaseDatabase.getInstance().getReference("this")
      CoroutineScope(Dispatchers.IO).launch {
         db.addValueEventListener(
            object : ValueEventListener {
               override fun onDataChange(snapshot: DataSnapshot) {
                  when{
                     snapshot.exists() -> {
                        arraylist.clear()
                        for (snapshot in snapshot.children){
                           val task = snapshot.getValue(Task::class.java)
                           arraylist.add(task!!)
                        }
                        recyclerView.adapter = TaskAdapter(arraylist)
                     }
                  }
               }

               override fun onCancelled(error: DatabaseError) {
                  Log.d("onCancelled", "$error")
               }

            }
         )
      }
   }

}