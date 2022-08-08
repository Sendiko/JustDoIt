package com.sendiko.justdoit.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.sendiko.justdoit.R
import com.sendiko.justdoit.dataStore1
import com.sendiko.justdoit.databinding.FragmentDashboardBinding
import com.sendiko.justdoit.repository.model.Task
import com.sendiko.justdoit.repository.preferences.AuthPreferences
import com.sendiko.justdoit.repository.preferences.AuthViewModel
import com.sendiko.justdoit.repository.preferences.AuthViewModelFactory

class DashboardFragment : Fragment() {

   private var _binding: FragmentDashboardBinding? = null
   private val binding get() = _binding!!

   private val dashboardViewModel : DashboardViewModel by activityViewModels()

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

      dashboardViewModel.isFailed.observe(viewLifecycleOwner){
         when(it.isFailed){
            true -> showSnackbar(it.errorMessage.toString())
            else -> null
         }
      }

      dashboardViewModel.isLoading.observe(viewLifecycleOwner){
         when{
            it -> binding.progressBar2.isVisible = true
            else -> binding.progressBar2.isVisible = false
         }
      }

      dashboardViewModel.isEmpty.observe(viewLifecycleOwner){
         when{
            it -> {
               binding.imageView3.isVisible = true
               binding.textSwipe3.isVisible = true
               binding.rvTaskChecked.isVisible = false
            }
            else -> {
               binding.imageView3.isVisible = false
               binding.textSwipe3.isVisible = false
               binding.rvTaskChecked.isVisible = true
            }
         }
      }

      binding.swipeRefresh.setOnRefreshListener {
         requireActivity().recreate()
      }

      binding.buttonAdd.setOnClickListener {
         showInputSheet()
      }

      binding.buttonSettings.setOnClickListener {
         findNavController().navigate(R.id.action_navigation_dashboard_to_navigation_notifications)
      }

   }

   private fun showSnackbar(message : String){
      Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
   }

   private fun showInputSheet(){
      val inputSheet = BottomSheetDialog(requireContext())
      val view = layoutInflater.inflate(R.layout.fragment_task, null)
      inputSheet.setContentView(view)
      inputSheet.show()

      val inputTask = view.findViewById<TextInputEditText>(R.id.input_task)
      val inputSubject = view.findViewById<TextInputEditText>(R.id.input_subject)
      val buttonCancel = view.findViewById<Button>(R.id.button_cancel)
      val buttonSubmit = view.findViewById<Button>(R.id.button_submit)

      buttonCancel.setOnClickListener {
         inputSheet.dismiss()
      }

      buttonSubmit.setOnClickListener {
         val t = inputTask.text.toString()
         val s = inputSubject.text.toString()
      }

   }

   private fun setupRecyclerView(){
      val taskList = arrayListOf<Task>()
      val rv = binding.rvTaskChecked
      rv.layoutManager = LinearLayoutManager(context)
      rv.setHasFixedSize(true)
   }

   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
   }
}