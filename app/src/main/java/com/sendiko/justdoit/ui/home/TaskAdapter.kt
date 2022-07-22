package com.sendiko.justdoit.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.sendiko.justdoit.R
import com.sendiko.justdoit.databinding.CardItemTaskBinding
import com.sendiko.justdoit.model.Task

class TaskAdapter(private val task : ArrayList<Task>, context : Context) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>(){

   class TaskViewHolder(var binding: CardItemTaskBinding) : RecyclerView.ViewHolder(binding.root)

   private val _precessIsDone = MutableLiveData<Boolean>()
   val processIsDone : LiveData<Boolean> = _precessIsDone

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
      val db = FirebaseDatabase.getInstance().getReference("this")
      val db2 = FirebaseDatabase.getInstance().getReference("this_checked")
      val tasks = Task(task.id, task.task, task.subject, true)
      db2.child(task.id.toString()).setValue(tasks).addOnCompleteListener {
         db.child(task.id.toString()).removeValue()
      }
   }

}