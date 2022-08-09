package com.sendiko.justdoit.ui.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.sendiko.justdoit.R
import com.sendiko.justdoit.dataStore1
import com.sendiko.justdoit.databinding.FragmentDashboardBinding
import com.sendiko.justdoit.repository.ViewModelFactory
import com.sendiko.justdoit.repository.model.Task
import com.sendiko.justdoit.repository.preferences.AuthPreferences
import com.sendiko.justdoit.repository.preferences.AuthViewModel
import com.sendiko.justdoit.repository.preferences.AuthViewModelFactory
import com.sendiko.justdoit.ui.home.TaskAdapter
import com.sendiko.justdoit.ui.task.TaskViewModel

class DashboardFragment : Fragment() {

   private var _binding: FragmentDashboardBinding? = null
   private val binding get() = _binding!!

   private val dashboardViewModel : DashboardViewModel by activityViewModels()

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

      taskViewModel.allTasks.observe(viewLifecycleOwner){
         setupRecyclerView(it)
      }


      binding.swipeRefresh.setOnRefreshListener {
         requireActivity().recreate()
      }

      binding.buttonAdd.setOnClickListener {
         showInputSheet()
      }

      binding.buttonSettings.setOnClickListener {
         findNavController().navigate(R.id.action_navigation_dashboard_to_navigation_notifications)
      }

   }

   private fun showSnackbar(message : String){
      Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
   }

   private fun showInputSheet(){
      val inputSheet = BottomSheetDialog(requireContext())
      val view = layoutInflater.inflate(R.layout.fragment_task, null)
      inputSheet.setContentView(view)
      inputSheet.show()

      val inputTask = view.findViewById<TextInputEditText>(R.id.input_task)
      val inputSubject = view.findViewById<TextInputEditText>(R.id.input_subject)
      val buttonCancel = view.findViewById<Button>(R.id.button_cancel)
      val buttonSubmit = view.findViewById<Button>(R.id.button_submit)

      buttonCancel.setOnClickListener {
         inputSheet.dismiss()
      }

      buttonSubmit.setOnClickListener {
         val t = inputTask.text.toString()
         val s = inputSubject.text.toString()
         val task = Task(0, t, s, false)
         taskViewModel.insertTask(task)
         inputSheet.dismiss()
      }

   }

   private fun setupRecyclerView(taskList : List<Task>){
      val task = arrayListOf<Task>()
      val rv = binding.rvTaskChecked
      rv.layoutManager = LinearLayoutManager(context)
      val rvAdapter = TaskAdapter(task, requireContext())
      rv.adapter = rvAdapter
      rvAdapter.updateList(taskList)
      rv.setHasFixedSize(true)
   }

   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
   }
}