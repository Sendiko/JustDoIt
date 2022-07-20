package com.sendiko.justdoit.ui.dashboard

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
import com.sendiko.justdoit.databinding.FragmentDashboardBinding
import com.sendiko.justdoit.model.Task
import com.sendiko.justdoit.repository.preferences.AuthPreferences
import com.sendiko.justdoit.repository.preferences.AuthViewModel
import com.sendiko.justdoit.repository.preferences.AuthViewModelFactory
import com.sendiko.justdoit.ui.home.TaskAdapter

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
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
        val dashboardViewModel =
            ViewModelProvider(this)[DashboardViewModel::class.java]

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

        binding.buttonAdd.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_dashboard_to_taskFragment2)
        }

        binding.buttonSettings.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_dashboard_to_navigation_notifications)
        }

    }

    private fun setupRecyclerView(){
        val taskList = arrayListOf<Task>()
        val rv = binding.rvTaskChecked
        rv.layoutManager = LinearLayoutManager(context)
        rv.setHasFixedSize(true)
        rv.adapter = TaskAdapter(taskList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}