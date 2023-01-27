package com.sendiko.justdoit.ui.task.showtask

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sendiko.justdoit.databinding.FragmentShowTaskBinding

class ShowTaskFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentShowTaskBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentShowTaskBinding.inflate(layoutInflater)
        return binding.root
    }
}