package com.sendiko.justdoit.ui.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sendiko.justdoit.databinding.FragmentUpdateDialogBinding

class UpdateDialogFragment : BottomSheetDialogFragment() {

   private var _binding: FragmentUpdateDialogBinding? = null
   private val binding get() = _binding!!

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {

      _binding = FragmentUpdateDialogBinding.inflate(inflater, container, false)
      return binding.root

   }

   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
   }
}