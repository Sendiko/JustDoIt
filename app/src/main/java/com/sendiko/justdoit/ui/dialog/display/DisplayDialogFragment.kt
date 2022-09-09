package com.sendiko.justdoit.ui.dialog.display

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sendiko.justdoit.databinding.FragmentDisplayDialogBinding

class DisplayDialogFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentDisplayDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDisplayDialogBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TODO: CODE HERE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}