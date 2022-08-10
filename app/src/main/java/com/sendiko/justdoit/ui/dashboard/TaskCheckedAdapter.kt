package com.sendiko.justdoit.ui.dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sendiko.justdoit.databinding.CardItemTaskCheckedBinding
import com.sendiko.justdoit.repository.model.Task

private const val TAG = "TaskCheckedAdapter"
class TaskCheckedAdapter(private val task : ArrayList<Task>, context: Context, private val onClick : onItemClickListener) :
   RecyclerView.Adapter<TaskCheckedAdapter.Task2ViewHolder>() {

   class Task2ViewHolder(var binding : CardItemTaskCheckedBinding) : RecyclerView.ViewHolder(binding.root)

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Task2ViewHolder {
      val binding = CardItemTaskCheckedBinding.inflate(LayoutInflater.from(parent.context))
      return Task2ViewHolder(binding)
   }

   override fun onBindViewHolder(holder: Task2ViewHolder, position: Int) {
      val currentItem = task[position]
      holder.binding.task.text = currentItem.task
      holder.binding.subjectTask.text = currentItem.subject
      holder.binding.task.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
      holder.binding.subjectTask.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG

      holder.binding.checkbox.setOnClickListener {
         onClick.onUncheckListener(currentItem)
      }

      holder.binding.delete.setOnClickListener {
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
      fun onUncheckListener(task: Task)
      fun onDeleteListener(task: Task)
   }

}