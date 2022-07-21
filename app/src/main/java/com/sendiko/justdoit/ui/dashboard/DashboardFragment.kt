package com.sendiko.justdoit.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sendiko.justdoit.R
import com.sendiko.justdoit.dataStore1
import com.sendiko.justdoit.databinding.FragmentDashboardBinding
import com.sendiko.justdoit.model.Task
import com.sendiko.justdoit.model.Task2
import com.sendiko.justdoit.repository.preferences.AuthPreferences
import com.sendiko.justdoit.repository.preferences.AuthViewModel
import com.sendiko.justdoit.repository.preferences.AuthViewModelFactory
import com.sendiko.justdoit.ui.home.TaskAdapter

class DashboardFragment : Fragment() {

   private var _binding: FragmentDashboardBinding? = null
   private val binding get() = _binding!!

   private val dashboardViewModel : DashboardViewModel by activityViewModels()

   private val pref by lazy{
      val context = requireNotNull(this.context)
      AuthPreferences.getInstance(context.dataStore1)
   }

   private val authViewModel : AuthViewModel by lazy {
      ViewModelProvider(this, AuthViewModelFactory(pref))[AuthViewModel::class.java]
   }

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
      setupRecyclerView()

      dashboardViewModel.isLoading.observe(viewLifecycleOwner){
         when{
            it -> binding.progressBar2.isVisible = true
            else -> binding.progressBar2.isVisible = false
         }
      }

      dashboardViewModel.isEmpty.observe(viewLifecycleOwner){
         when{
            it -> binding.imageView3.isVisible = true
            else -> binding.imageView3.isVisible = false
         }
      }

      binding.buttonAdd.setOnClickListener {
         findNavController().navigate(R.id.action_navigation_dashboard_to_taskFragment2)
      }

      binding.buttonSettings.setOnClickListener {
         findNavController().navigate(R.id.action_navigation_dashboard_to_navigation_notifications)
      }

   }

   private fun setupRecyclerView(){
      val taskList = arrayListOf<Task2>()
      val rv = binding.rvTaskChecked
      rv.layoutManager = LinearLayoutManager(context)
      rv.setHasFixedSize(true)
      dashboardViewModel.getTaskData(taskList, rv)
   }

   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
   }
}