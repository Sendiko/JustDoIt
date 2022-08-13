package com.sendiko.justdoit.ui.dashboard

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sendiko.justdoit.databinding.FragmentDashboardBinding
import com.sendiko.justdoit.repository.ViewModelFactory
import com.sendiko.justdoit.repository.model.Task
import com.sendiko.justdoit.repository.preferences.AuthPreferences
import com.sendiko.justdoit.repository.preferences.AuthViewModel
import com.sendiko.justdoit.repository.preferences.AuthViewModelFactory
import com.sendiko.justdoit.ui.container.SettingActivity
import com.sendiko.justdoit.ui.container.dataStore1
import com.sendiko.justdoit.ui.task.TaskViewModel

class DashboardFragment : Fragment() {

   private var _binding: FragmentDashboardBinding? = null
   private val binding get() = _binding!!

   private val taskViewModel : TaskViewModel by lazy {
      val activity = requireNotNull(this.activity)
      getViewModel(activity)
   }

   private fun getViewModel(activity: FragmentActivity) : TaskViewModel {
      val factory = ViewModelFactory.getInstance(activity.application)
      return ViewModelProvider(this, factory)[TaskViewModel::class.java]
   }

   private val pref by lazy{
      val context = requireNotNull(this.context)
      AuthPreferences.getInstance(context.dataStore1)
   }

   private val authViewModel : AuthViewModel by lazy {
      ViewModelProvider(this, AuthViewModelFactory(pref))[AuthViewModel::class.java]
   }

   @SuppressLint("SetTextI18n")
   override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {

      _binding = FragmentDashboardBinding.inflate(inflater, container, false)
      val root: View = binding.root

      authViewModel.getUser().observe(viewLifecycleOwner){
         binding.greeting.text = "Great job, $it!"
      }

      return root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      taskViewModel.allCheckTasks.observe(viewLifecycleOwner){
         setupRecyclerView(it)
      }


      binding.swipeRefresh.setOnRefreshListener {
         requireActivity().recreate()
      }

      binding.buttonSettings.setOnClickListener {
         val intent = Intent(requireActivity(), SettingActivity::class.java)
         startActivity(intent)
      }

      alsoCheckIfEmpty()

   }

   private fun alsoCheckIfEmpty(){
      taskViewModel.alsoCheckIfEmpty.observe(viewLifecycleOwner){
         when(it){
            0 -> {
               binding.imageView3.visibility = View.VISIBLE
               binding.textSwipe3.visibility = View.VISIBLE
            }
            else -> {
               binding.imageView3.visibility = View.GONE
               binding.textSwipe3.visibility = View.GONE
            }
         }
      }
   }

   private fun setupRecyclerView(taskList : List<Task>){
      val rv = binding.rvTaskChecked
      rv.layoutManager = LinearLayoutManager(context)
      val rvAdapter = TaskCheckedAdapter(arrayListOf(), requireContext(), object : TaskCheckedAdapter.onItemClickListener{
         override fun onUncheckListener(task: Task) {
            taskViewModel.updateTask(Task(task.task, task.subject, "false"))
         }

         override fun onDeleteListener(task: Task) {
            taskViewModel.deleteTask(task)
         }

      })
      rv.adapter = rvAdapter
      rvAdapter.updateList(taskList)
      rv.setHasFixedSize(true)
   }

   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
   }
}