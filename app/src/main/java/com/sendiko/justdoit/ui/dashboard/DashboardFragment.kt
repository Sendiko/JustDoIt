package com.sendiko.justdoit.ui.dashboard

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sendiko.justdoit.R
import com.sendiko.justdoit.databinding.FragmentDashboardBinding
import com.sendiko.justdoit.repository.Constant
import com.sendiko.justdoit.repository.ViewModelFactory
import com.sendiko.justdoit.repository.model.Task
import com.sendiko.justdoit.repository.preferences.AuthPreferences
import com.sendiko.justdoit.repository.preferences.AuthViewModel
import com.sendiko.justdoit.repository.preferences.AuthViewModelFactory
import com.sendiko.justdoit.repository.preferences.SettingsPreference
import com.sendiko.justdoit.ui.container.SettingActivity
import com.sendiko.justdoit.ui.container.dataStore1
import com.sendiko.justdoit.ui.settings.SettingsViewModel
import com.sendiko.justdoit.ui.task.TaskCheckedAdapter
import com.sendiko.justdoit.ui.task.TaskViewModel
import java.util.*

private const val TAG = "DashboardFragment"
class DashboardFragment : Fragment() {

   private var _binding: FragmentDashboardBinding? = null
   private val binding get() = _binding!!

   private val taskViewModel : TaskViewModel by lazy {
      getViewModel(requireNotNull(this.activity))
   }

   private fun getViewModel(activity: FragmentActivity) : TaskViewModel {
      return ViewModelProvider(
         this, ViewModelFactory
            .getInstance(activity.application)
      )[TaskViewModel::class.java]
   }

   private val pref by lazy{
      AuthPreferences
         .getInstance(requireNotNull(this.context).dataStore1)
   }

   private val authViewModel : AuthViewModel by lazy {
      ViewModelProvider(this, AuthViewModelFactory(pref))[AuthViewModel::class.java]
   }

   private val settingsPref by lazy {
      SettingsPreference.getInstance(requireNotNull(this.context).dataStore1)
   }

   private val settingsViewModel : SettingsViewModel by lazy {
      ViewModelProvider(this,
         SettingsViewModel.SettingsViewModelFactory(settingsPref)
      )[SettingsViewModel::class.java]
   }

   override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {
      _binding = FragmentDashboardBinding.inflate(inflater, container, false)

      return binding.root
   }

   @SuppressLint("SetTextI18n")
   override fun onViewCreated(
      view: View,
      savedInstanceState: Bundle?
   ) {
      super.onViewCreated(
         view,
         savedInstanceState
      )

      settingsViewModel.getDarkTheme().observe(viewLifecycleOwner){
         when(it){
            true -> {
               AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            false -> {
               AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
         }
      }

      settingsViewModel.getLanguage().observe(viewLifecycleOwner){
         setAppLocale(requireContext(), it)
      }

      taskViewModel.allCheckTasks.observe(viewLifecycleOwner){
         setupRecyclerView(it)
      }

      authViewModel.getUser().observe(viewLifecycleOwner){
         binding.toolbar.title = getString(R.string.greeting3) + it + "!"
      }

      binding.swipeRefresh.setOnRefreshListener {
         requireActivity().recreate()
      }

      binding.buttonSettings.setOnClickListener {
         startActivity(Intent(requireActivity(), SettingActivity::class.java))
      }

      alsoCheckIfEmpty()

   }

   private fun alsoCheckIfEmpty(){
      taskViewModel.alsoCheckIfEmpty.observe(viewLifecycleOwner){
         when(it){
            0 -> {
               binding.imageView3.visibility = View.VISIBLE
               binding.textSwipe3.visibility = View.VISIBLE
            }
            else -> {
               binding.imageView3.visibility = View.GONE
               binding.textSwipe3.visibility = View.GONE
            }
         }
      }
   }

   private fun setupRecyclerView(taskList : List<Task>){
      val rv = binding.rvTaskChecked
      rv.layoutManager = LinearLayoutManager(context)
      val rvAdapter = TaskCheckedAdapter(
         arrayListOf(),
         requireContext(),
         object : TaskCheckedAdapter.OnItemClickListener{
            override fun onUncheckListener(task: Task) {
               Handler(Looper.myLooper()!!).postDelayed({
                  taskViewModel.updateTask(
                     Task(
                        task.id, task.task, task.subject, task.priority, Constant.mFalse
                     )
                  )
               }, 200)
               Toast.makeText(context, "${task.task} is unchecked", Toast.LENGTH_SHORT)
                  .show()
            }

            override fun onDeleteListener(task: Task) {
               taskViewModel.deleteTask(task)
               Toast.makeText(context, "${task.task} is deleted", Toast.LENGTH_SHORT)
                  .show()
            }

         }
      )
      rv.adapter = rvAdapter
      rvAdapter.updateList(taskList)
      rv.setHasFixedSize(true)
   }

   private fun setAppLocale(
      context: Context,
      language: String
   ) {
      val locale = Locale(language)
      Locale.setDefault(locale)
      val config = context.resources.configuration
      config.setLocale(locale)
      context.createConfigurationContext(config)
      context.resources.updateConfiguration(config, context.resources.displayMetrics)
   }

   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
   }
}