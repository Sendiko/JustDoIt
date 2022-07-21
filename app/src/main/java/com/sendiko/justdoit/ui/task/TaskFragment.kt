package com.sendiko.justdoit.ui.task

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.sendiko.justdoit.R
import com.sendiko.justdoit.databinding.FragmentTaskBinding

class TaskFragment : Fragment() {

   private var _binding : FragmentTaskBinding?= null
   private val binding get() = _binding!!

   private val taskViewModel : TaskViewModel by activityViewModels()

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {
      _binding = FragmentTaskBinding.inflate(layoutInflater)

      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      binding.icCancel.setOnClickListener {
         findNavController().navigate(R.id.action_taskFragment2_to_navigation_home)
      }

      binding.buttonSave.setOnClickListener {
         val t = binding.inputTask.text.toString()
         val s = binding.inputSubject.text.toString()
         when{
            taskViewModel.insertTask(t, s) -> findNavController().navigate(R.id.action_taskFragment2_to_navigation_home)
            else -> Toast.makeText(context, "failed", Toast.LENGTH_SHORT).show()
         }
      }

   }

}