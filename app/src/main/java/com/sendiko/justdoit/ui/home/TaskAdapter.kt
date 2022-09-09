package com.sendiko.justdoit.ui.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sendiko.justdoit.databinding.CardItemTaskBinding
import com.sendiko.justdoit.repository.model.Task

private const val TAG = "TaskAdapter"
class TaskAdapter(private val task: ArrayList<Task>, private val onClick: OnItemClickListener) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>(){

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
         onClick.onUpdateClickListener(currentItem)
      }

      holder.binding.checkbox.setOnClickListener {
         onClick.onCheckListener(currentItem)
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
   }

   interface OnItemClickListener {
      fun onCheckListener(task: Task)
      fun onUpdateClickListener(task: Task)
   }

}