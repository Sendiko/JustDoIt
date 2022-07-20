package com.sendiko.justdoit.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sendiko.justdoit.R
import com.sendiko.justdoit.databinding.CardItemTaskBinding
import com.sendiko.justdoit.model.Task

class TaskAdapter(private val task : ArrayList<Task>) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>(){

   class TaskViewHolder(var binding: CardItemTaskBinding) : RecyclerView.ViewHolder(binding.root)

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
      val binding = CardItemTaskBinding.inflate(LayoutInflater.from(parent.context))
      return TaskViewHolder(binding)
   }

   override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
      val currentItem = task[position]
      holder.binding.task.text = currentItem.task
      holder.binding.subjectTask.text = currentItem.subject
      when{
         !currentItem.isDone -> {
            holder.binding.checkbox.setImageResource(R.drawable.ic_checked_empty)
            holder.binding.edit.setImageResource(R.drawable.ic_edit)
         }
         currentItem.isDone -> {
            holder.binding.checkbox.setImageResource(R.drawable.ic_checkbox_filled)
            holder.binding.edit.setImageResource(R.drawable.ic_trash_icon)
         }
      }
   }

   override fun getItemCount(): Int {
      return task.size
   }
}