package com.sendiko.justdoit.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.sendiko.justdoit.R
import com.sendiko.justdoit.databinding.FragmentHomeBinding
import com.sendiko.justdoit.model.Task

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

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
            showInputFragment()
        }

    }

    private fun showInputFragment() {
        val inputFrag = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.fragment_input_task, null)
        inputFrag.setContentView(view)
        inputFrag.show()
    }

    private fun setupRecyclerView(){
        val taskList = arrayListOf<Task>(
            Task(
                "Publish an android app",
                "My career path",
                false
            ),
            Task(
                "Improved my skills",
                "My career path",
                false
            )
        )
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