package com.sendiko.justdoit.ui.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sendiko.justdoit.databinding.FragmentInputTaskBinding

class InputTaskFragment : BottomSheetDialogFragment() {

    private var _binding : FragmentInputTaskBinding?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentInputTaskBinding.inflate(layoutInflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TODO : CODE HERE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}