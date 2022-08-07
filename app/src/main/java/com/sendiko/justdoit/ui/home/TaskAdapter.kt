package com.sendiko.justdoit.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.sendiko.justdoit.databinding.CardItemTaskBinding
import com.sendiko.justdoit.repository.model.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskAdapter(private val task : ArrayList<Task>, context : Context) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>(){

   class TaskViewHolder(var binding: CardItemTaskBinding) : RecyclerView.ViewHolder(binding.root)

   private lateinit var auth : FirebaseAuth

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
      val binding = CardItemTaskBinding.inflate(LayoutInflater.from(parent.context))
      return TaskViewHolder(binding)
   }

   override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
      val currentItem = task[position]
      holder.binding.task.text = currentItem.task
      holder.binding.subjectTask.text = currentItem.subject

      holder.binding.checkbox.setOnClickListener {
         checkTask(currentItem)
      }

   }

   override fun getItemCount(): Int {
      return task.size
   }

   private fun checkTask(task: Task){
      CoroutineScope(Dispatchers.IO).launch {
         auth = Firebase.auth
         val user = auth.currentUser
         val db = FirebaseDatabase.getInstance().getReference("${user?.uid}_this")
         val db2 = FirebaseDatabase.getInstance().getReference("${user?.uid}_this_checked")
         val tasks = Task(task.id, task.task, task.subject, true)
         db2.child(task.id.toString()).setValue(tasks).addOnCompleteListener {
            db.child(task.id.toString()).removeValue()
         }
      }
   }
}