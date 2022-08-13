package com.sendiko.justdoit.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sendiko.justdoit.databinding.CardItemTaskBinding
import com.sendiko.justdoit.repository.model.Task

private const val TAG = "TaskAdapter"
class TaskAdapter(private val task : ArrayList<Task>, context : Context, private val onClick : onItemClickListener) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>(){

   class TaskViewHolder(var binding: CardItemTaskBinding) : RecyclerView.ViewHolder(binding.root)

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
      val binding = CardItemTaskBinding.inflate(LayoutInflater.from(parent.context))
      return TaskViewHolder(binding)
   }

   override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
      val currentItem = task[position]
      holder.binding.task.text = currentItem.task
      holder.binding.subjectTask.text = currentItem.subject

      holder.binding.cardTask.setOnClickListener {
         onClick.onTaskClickListener(currentItem)
      }

      holder.binding.checkbox.setOnClickListener {
         onClick.onCheckListener(currentItem)
      }

      holder.binding.update.setOnClickListener {
         onClick.onDeleteListener(currentItem)
      }

   }

   override fun getItemCount(): Int {
      return task.size
   }

   @SuppressLint("NotifyDataSetChanged")
   fun updateList(newList : List<Task>) {
      task.clear()
      task.addAll(newList)
      notifyDataSetChanged()
      Log.d(TAG, "updateList: $newList")
   }

   interface onItemClickListener {
      fun onCheckListener(task: Task)
      fun onDeleteListener(task: Task)
      fun onTaskClickListener(task: Task)
   }

}