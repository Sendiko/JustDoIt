package com.sendiko.justdoit.ui.task

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
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

   override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
      val dialog = super.onCreateDialog(savedInstanceState)
      dialog.setOnShowListener {
         val parentLayout = binding.root
         parentLayout.let { sheet ->
            val behavior = BottomSheetBehavior.from(sheet)
            val layoutParams = sheet.layoutParams
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
            sheet.layoutParams = layoutParams
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
         }
      }
      return dialog
   }

}