package com.sendiko.justdoit.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.sendiko.justdoit.R
import com.sendiko.justdoit.databinding.FragmentHomeBinding
import com.sendiko.justdoit.repository.SharedViewModel
import com.sendiko.justdoit.repository.ViewModelFactory
import com.sendiko.justdoit.repository.model.StringConstants
import com.sendiko.justdoit.repository.model.Task
import com.sendiko.justdoit.repository.preferences.AuthPreferences
import com.sendiko.justdoit.repository.preferences.AuthViewModel
import com.sendiko.justdoit.repository.preferences.AuthViewModelFactory
import com.sendiko.justdoit.ui.container.SettingActivity
import com.sendiko.justdoit.ui.container.dataStore
import com.sendiko.justdoit.ui.home.TaskAdapter.*
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
         binding.toolbar.title = "Hi, $it!"
      }

      binding.buttonSettings.setOnClickListener {
         val intent = Intent(requireActivity(), SettingActivity::class.java)
         startActivity(intent)
      }

      binding.swipeRefresh.setOnRefreshListener {
         requireActivity().recreate()
      }

      checkIfEmpty()
      onBackPressed()

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
   private fun showUpdateSheet(tasks: Task){
      val inputSheet = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
      val view = layoutInflater.inflate(R.layout.fragment_task, null)
      inputSheet.setContentView(view)
      inputSheet.show()

      val layoutTask = view.findViewById<TextInputLayout>(R.id.layout_task)
      val inputTask = view.findViewById<TextInputEditText>(R.id.input_task)
      val inputSubject = view.findViewById<TextInputEditText>(R.id.input_subject)
      val buttonSubmit = view.findViewById<Button>(R.id.button_submit)
      val headerTitle = view.findViewById<TextView>(R.id.header_title)
      val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)
      var categories = ""

      inputTask.setText(tasks.task)
      inputSubject.setText(tasks.subject)
      buttonSubmit.text = StringConstants.update
      headerTitle.text = StringConstants.updateTask

      radioGroup.setOnCheckedChangeListener { _, item ->
         when(item) {
            R.id.button_sport -> categories = StringConstants.categoriesSport
            R.id.button_work -> categories = StringConstants.categoriesWork
            R.id.button_game -> categories = StringConstants.categoriesGame
            R.id.button_school -> categories = StringConstants.categoriesSchool
            R.id.button_chores -> categories = StringConstants.categoriesChores
         }
      }

      buttonSubmit.setOnClickListener {
         val task = inputTask.text.toString()
         val sub = inputSubject.text.toString()
         when {
            task.isNotEmpty() -> {
               val task = Task(tasks.id, task, sub, categories, StringConstants.falsee)
               taskViewModel.insertTask(task)
               inputSheet.dismiss()
            }
            else -> {
               layoutTask.error = StringConstants.emptyError
               inputTask.background = AppCompatResources.getDrawable(requireContext(), R.drawable.box_background_error)
            }
         }
      }
   }

   private fun setupRecyclerView(taskList : List<Task>){
      val rv = binding.rvTask
      val rvAdapter = TaskAdapter(arrayListOf(), object : OnItemClickListener{
         override fun onCheckListener(task: Task) {
            taskViewModel.updateTask(Task(task.id, task.task, task.subject, task.categories, StringConstants.truee))
            Toast.makeText(context, "${task.task} is checked", Toast.LENGTH_SHORT).show()
         }

         override fun onTaskClickListener(task: Task) {
            showUpdateSheet(task)
         }

      })
      rvAdapter.updateList(taskList)
      rv.apply {
         adapter = rvAdapter
         layoutManager = LinearLayoutManager(context)
         setHasFixedSize(true)
      }
   }

   private fun onBackPressed(){
      requireActivity().onBackPressedDispatcher.addCallback {
         Toast.makeText(context, StringConstants.backToast, Toast.LENGTH_SHORT).show()
      }
   }

   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
   }
}