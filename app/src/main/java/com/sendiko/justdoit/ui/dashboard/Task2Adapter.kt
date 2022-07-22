package com.sendiko.justdoit.ui.dashboard

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.sendiko.justdoit.databinding.CardItemTaskCheckedBinding
import com.sendiko.justdoit.model.Task
import com.sendiko.justdoit.model.Task2

class Task2Adapter(private val task2 : ArrayList<Task2>) :
   RecyclerView.Adapter<Task2Adapter.Task2ViewHolder>() {

   class Task2ViewHolder(var binding : CardItemTaskCheckedBinding) : RecyclerView.ViewHolder(binding.root)

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Task2ViewHolder {
      val binding = CardItemTaskCheckedBinding.inflate(LayoutInflater.from(parent.context))
      return Task2ViewHolder(binding)
   }

   override fun onBindViewHolder(holder: Task2ViewHolder, position: Int) {
      val currentItem = task2[position]
      holder.binding.task.text = currentItem.task
      holder.binding.subjectTask.text = currentItem.subject
      holder.binding.task.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
      holder.binding.subjectTask.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

      holder.binding.checkbox.setOnClickListener {
         unCheckTask(currentItem)
      }

      holder.binding.delete.setOnClickListener {
         deleteTask(currentItem)
      }

   }

   override fun getItemCount(): Int {
      return task2.size
   }

   private fun unCheckTask(task2: Task2){
      val db = FirebaseDatabase.getInstance().getReference("this")
      val db2 = FirebaseDatabase.getInstance().getReference("this_checked")
      val tasks = Task(task2.id, task2.task, task2.subject, true)
      db.child(task2.id.toString()).setValue(tasks).addOnCompleteListener {
         db2.child(task2.id.toString()).removeValue()
      }
   }

   private fun deleteTask(task2: Task2){
      val db = FirebaseDatabase.getInstance().getReference("this_checked")
      db.child(task2.id.toString()).removeValue()
   }

}