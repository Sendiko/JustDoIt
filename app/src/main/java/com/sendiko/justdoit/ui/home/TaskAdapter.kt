package com.sendiko.justdoit.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.sendiko.justdoit.R
import com.sendiko.justdoit.databinding.CardItemTaskBinding
import com.sendiko.justdoit.repository.Constant
import com.sendiko.justdoit.repository.model.Task

private const val TAG = "TaskAdapter"
class TaskAdapter(
   private val task: ArrayList<Task>,
   private val context: Context,
   private val onClick: OnItemClickListener
   ) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>(){

   class TaskViewHolder(var binding: CardItemTaskBinding) : RecyclerView.ViewHolder(binding.root)

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
      val binding = CardItemTaskBinding.inflate(LayoutInflater.from(parent.context))
      return TaskViewHolder(binding)
   }

   override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
      val currentItem = task[position]
      holder.binding.task.text = currentItem.task
      holder.binding.subjectTask.text = currentItem.subject

      when(currentItem.priority){
         Constant.mImportant -> holder.binding.circlePriority.setImageResource(R.drawable.circle_important)
         Constant.mNeedToBeDone -> holder.binding.circlePriority.setImageResource(R.drawable.circle_needtobedone)
         Constant.mCanDoItAnytime -> holder.binding.circlePriority.setImageResource(R.drawable.circle_candoitanytime)
      }

      holder.binding.cardTask.setOnClickListener {
         onClick.onUpdateClickListener(currentItem)
      }

      holder.binding.checkbox.setOnCheckedChangeListener { _, _ ->
         holder.binding.root.startAnimation(AnimationUtils.loadAnimation(context, R.anim.activity_fade_out))
         holder.binding.root.isVisible = false
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