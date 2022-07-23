package com.sendiko.justdoit.ui.task

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sendiko.justdoit.R
import com.sendiko.justdoit.databinding.FragmentTaskBinding

class TaskFragment : BottomSheetDialogFragment() {

   private var _binding : FragmentTaskBinding?= null
   val binding get() = _binding!!

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {
      _binding = FragmentTaskBinding.inflate(layoutInflater)
      return binding.root
   }
}