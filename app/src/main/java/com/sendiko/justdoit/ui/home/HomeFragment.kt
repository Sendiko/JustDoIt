package com.sendiko.justdoit.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sendiko.justdoit.R
import com.sendiko.justdoit.dataStore1
import com.sendiko.justdoit.databinding.FragmentHomeBinding
import com.sendiko.justdoit.model.Task
import com.sendiko.justdoit.repository.ViewModelFactory
import com.sendiko.justdoit.repository.preferences.AuthPreferences
import com.sendiko.justdoit.repository.preferences.AuthViewModel
import com.sendiko.justdoit.repository.preferences.AuthViewModelFactory
import com.sendiko.justdoit.ui.dashboard.DashboardViewModel

class HomeFragment : Fragment() {

   private var _binding: FragmentHomeBinding? = null
   private val binding get() = _binding!!

   private lateinit var task : ArrayList<Task>

   private val pref by lazy{
      val context = requireNotNull(this.context)
      AuthPreferences.getInstance(context.dataStore1)
   }

   private val authViewModel : AuthViewModel by lazy {
      ViewModelProvider(this, AuthViewModelFactory(pref))[AuthViewModel::class.java]
   }

   private val homeViewModel by lazy {
      val activity = requireNotNull(this.activity)
      getViewModel(activity)
   }

   private fun getViewModel(activity: FragmentActivity) :HomeViewModel {
      val factory = ViewModelFactory.getInstance(activity.application)
      return ViewModelProvider(this, factory)[HomeViewModel::class.java]
   }

   override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {

      _binding = FragmentHomeBinding.inflate(inflater, container, false)

      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      setupRecyclerView()

      homeViewModel.isLoading.observe(viewLifecycleOwner){
         when{
            it -> binding.progressBar.isVisible= true
            else -> binding.progressBar.isVisible = false
         }
      }

      homeViewModel.isEmpty.observe(viewLifecycleOwner){
         when{
            it -> {
               binding.imageView.isVisible = true
            }
            else -> binding.imageView.isVisible = false
         }
      }

      authViewModel.getUser().observe(viewLifecycleOwner){
         binding.greeting.text = "Hi, $it!"
      }

      binding.buttonAdd.setOnClickListener {
         findNavController().navigate(R.id.action_navigation_home_to_taskFragment2)
      }

      binding.buttonSettings.setOnClickListener {
         findNavController().navigate(R.id.action_navigation_home_to_navigation_notifications)
      }

      binding.swipeRefresh.setOnRefreshListener {
         requireActivity().recreate()
      }

   }

   private fun setupRecyclerView(){
      val taskList = arrayListOf<Task>()
      val rv = binding.rvTask
      rv.layoutManager = LinearLayoutManager(context)
      rv.setHasFixedSize(true)
      homeViewModel.getTaskData(taskList, rv)
   }

   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
   }
}