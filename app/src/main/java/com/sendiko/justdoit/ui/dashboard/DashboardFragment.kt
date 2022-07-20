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
import com.sendiko.justdoit.databinding.FragmentDashboardBinding
import com.sendiko.justdoit.model.Task
import com.sendiko.justdoit.ui.home.TaskAdapter

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this)[DashboardViewModel::class.java]

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

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