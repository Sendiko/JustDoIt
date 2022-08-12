package com.sendiko.justdoit.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.sendiko.justdoit.databinding.FragmentHomeBinding
import com.sendiko.justdoit.repository.ViewModelFactory
import com.sendiko.justdoit.repository.model.Task
import com.sendiko.justdoit.repository.preferences.AuthPreferences
import com.sendiko.justdoit.repository.preferences.AuthViewModel
import com.sendiko.justdoit.repository.preferences.AuthViewModelFactory
import com.sendiko.justdoit.ui.container.SettingActivity
import com.sendiko.justdoit.ui.container.dataStore
import com.sendiko.justdoit.ui.task.TaskViewModel

private const val TAG = "HomeFragment"
class HomeFragment : Fragment() {

   private var _binding: FragmentHomeBinding? = null
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
      AuthPreferences.getInstance(context.dataStore)
   }

   private val authViewModel : AuthViewModel by lazy {
      ViewModelProvider(this, AuthViewModelFactory(pref))[AuthViewModel::class.java]
   }

   override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {
      _binding = FragmentHomeBinding.inflate(inflater, container, false)
      return binding.root
   }

   @SuppressLint("SetTextI18n")
   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      taskViewModel.allTasks.observe(viewLifecycleOwner){
         setupRecyclerView(it)
      }


      authViewModel.getUser().observe(viewLifecycleOwner){
         binding.greeting.text = "Hi, $it!"
      }

      binding.buttonSettings.setOnClickListener {
         val intent = Intent(requireActivity(), SettingActivity::class.java)
         startActivity(intent)
      }

      binding.swipeRefresh.setOnRefreshListener {
         requireActivity().recreate()
      }

   }

   private fun showSnackbar(message : String){
      Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
   }

   private fun setupRecyclerView(taskList : List<Task>){
      val rv = binding.rvTask
      val rvAdapter = TaskAdapter(arrayListOf(), requireContext(), object : TaskAdapter.onItemClickListener{
         override fun onCheckListener(task: Task) {
            taskViewModel.updateTask(Task(task.task, task.subject, "true"))
            taskViewModel.allTasks.observe(viewLifecycleOwner){
               Toast.makeText(context, "$it", Toast.LENGTH_SHORT).show()
            }
            Log.d(TAG, "onCheckListener: ${task.task}, ${task.subject}, ${task.isDone}")
         }

         override fun onDeleteListener(task: Task) {
            taskViewModel.deleteTask(task)
            Log.d(TAG, "onDeleteListener: $task")
         }

      })
      rvAdapter.updateList(taskList)
      rv.apply {
         adapter = rvAdapter
         layoutManager = LinearLayoutManager(context)
         setHasFixedSize(true)
      }
   }

   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
   }
}