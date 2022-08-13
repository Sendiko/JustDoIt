package com.sendiko.justdoit.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.sendiko.justdoit.R
import com.sendiko.justdoit.databinding.FragmentHomeBinding
import com.sendiko.justdoit.repository.SharedViewModel
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

   private val sharedViewModel : SharedViewModel by activityViewModels()

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

      checkIfEmpty()

   }

   private fun checkIfEmpty() {
      taskViewModel.checkIfEmpty.observe(viewLifecycleOwner){
         when(it){
            0 -> {
               binding.imageView.visibility = View.VISIBLE
               binding.textSwipe.visibility = View.VISIBLE
            }
            else -> {
               binding.imageView.visibility = View.GONE
               binding.textSwipe.visibility = View.GONE
            }
         }
      }
   }

   @SuppressLint("SetTextI18n")
   private fun showUpdateSheet(tasks: Task, u : String){
      val inputSheet = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
      val view = layoutInflater.inflate(R.layout.fragment_task, null)
      inputSheet.setContentView(view)
      inputSheet.show()

      val inputTask = view.findViewById<TextInputEditText>(R.id.input_task)
      val inputSubject = view.findViewById<TextInputEditText>(R.id.input_subject)
      val buttonCancel = view.findViewById<Button>(R.id.button_cancel)
      val buttonSubmit = view.findViewById<Button>(R.id.button_submit)
      val headerTitle = view.findViewById<TextView>(R.id.header_title)

      inputTask.setText(tasks.task)
      inputSubject.setText(tasks.subject)
      buttonSubmit.text = u
      headerTitle.text = "Update task"

      buttonCancel.setOnClickListener {
         inputSheet.dismiss()
      }

      buttonSubmit.setOnClickListener {
         val task = inputTask.text.toString()
         val sub = inputSubject.text.toString()
         val tasks = Task(tasks.id, task, sub, "false")
         taskViewModel.insertTask(tasks)
         inputSheet.dismiss()
      }

   }

   private fun setupRecyclerView(taskList : List<Task>){
      val rv = binding.rvTask
      val rvAdapter = TaskAdapter(arrayListOf(), requireContext(), object : TaskAdapter.onItemClickListener{
         override fun onCheckListener(task: Task) {
            taskViewModel.updateTask(Task(task.id, task.task, task.subject, "true"))
            taskViewModel.allTasks.observe(viewLifecycleOwner){
               Toast.makeText(context, "${task.id}, ${task.task}, ${task.subject}, ${task.isDone}", Toast.LENGTH_SHORT).show()
            }
            Log.d(TAG, "onCheckListener: ${task.id}, ${task.task}, ${task.subject}, ${task.isDone}")
         }

         override fun onDeleteListener(task: Task) {
            taskViewModel.deleteTask(task)
            Log.d(TAG, "onDeleteListener: ${task.id}, ${task.task}, ${task.subject}, ${task.isDone}")
         }

         override fun onTaskClickListener(task: Task) {
            showUpdateSheet(task, "Update")
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