package com.sendiko.justdoit.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sendiko.justdoit.R
import com.sendiko.justdoit.dataStore1
import com.sendiko.justdoit.databinding.FragmentHomeBinding
import com.sendiko.justdoit.model.Task
import com.sendiko.justdoit.repository.preferences.AuthPreferences
import com.sendiko.justdoit.repository.preferences.AuthViewModel
import com.sendiko.justdoit.repository.preferences.AuthViewModelFactory

class HomeFragment : Fragment() {

   private var _binding: FragmentHomeBinding? = null
   private val binding get() = _binding!!

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
      val homeViewModel =
         ViewModelProvider(this)[HomeViewModel::class.java]

      _binding = FragmentHomeBinding.inflate(inflater, container, false)

      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      setupRecyclerView()

      binding.buttonAdd.setOnClickListener {
         findNavController().navigate(R.id.action_navigation_home_to_taskFragment2)
      }

      authViewModel.getUser().observe(viewLifecycleOwner){
         binding.greeting.text = "Hi, $it!"
      }

      binding.buttonSettings.setOnClickListener {
         findNavController().navigate(R.id.action_navigation_home_to_navigation_notifications)
      }

   }

   private fun setupRecyclerView(){
      val taskList = arrayListOf<Task>()
      val rv = binding.rvTask
      rv.layoutManager = LinearLayoutManager(context)
      rv.setHasFixedSize(true)
      rv.adapter = TaskAdapter(taskList)
   }

   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
   }
}