package com.sendiko.justdoit.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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
//        when(currentItem.isDone){
//            holder.binding.checkbox.drawable = R.
//        }
    }

    override fun getItemCount(): Int {
        return task.size
    }
}