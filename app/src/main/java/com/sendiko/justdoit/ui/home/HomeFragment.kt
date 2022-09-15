package com.sendiko.justdoit.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.sendiko.justdoit.R
import com.sendiko.justdoit.databinding.FragmentHomeBinding
import com.sendiko.justdoit.repository.Constant
import com.sendiko.justdoit.repository.SharedViewModel
import com.sendiko.justdoit.repository.ViewModelFactory
import com.sendiko.justdoit.repository.model.Task
import com.sendiko.justdoit.repository.preferences.AuthPreferences
import com.sendiko.justdoit.repository.preferences.AuthViewModel
import com.sendiko.justdoit.repository.preferences.AuthViewModelFactory
import com.sendiko.justdoit.repository.preferences.SettingsPreference
import com.sendiko.justdoit.ui.container.SettingActivity
import com.sendiko.justdoit.ui.container.dataStore
import com.sendiko.justdoit.ui.container.dataStore1
import com.sendiko.justdoit.ui.home.TaskAdapter.OnItemClickListener
import com.sendiko.justdoit.ui.settings.SettingsViewModel
import com.sendiko.justdoit.ui.task.TaskViewModel
import java.util.*

private const val TAG = "HomeFragment"
class HomeFragment : Fragment() {

   private var _binding: FragmentHomeBinding? = null
   private val binding get() = _binding!!

   private val sharedViewModel : SharedViewModel by activityViewModels()

   private val taskViewModel : TaskViewModel by lazy {
      getViewModel(requireNotNull(this.activity))
   }

   private fun getViewModel(activity: FragmentActivity) : TaskViewModel {
      return ViewModelProvider(this, ViewModelFactory.getInstance(activity.application))[TaskViewModel::class.java]
   }

   private val pref by lazy{
      AuthPreferences.getInstance(requireNotNull(this.context).dataStore)
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
      _binding = FragmentHomeBinding.inflate(inflater, container, false)
      return binding.root
   }

   @SuppressLint("SetTextI18n")
   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

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

      taskViewModel.allTasks.observe(viewLifecycleOwner){
         setupRecyclerView(it)
      }

      authViewModel.getUser().observe(viewLifecycleOwner){
         binding.toolbar.title = getString(R.string.greeting) + it + "!"
      }

      binding.buttonSettings.setOnClickListener {
         startActivity(Intent(requireActivity(), SettingActivity::class.java))
      }

      binding.swipeRefresh.setOnRefreshListener {
         requireActivity().recreate()
      }

      checkIfEmpty()
      onBackPressed()

   }

   private fun checkIfEmpty() {
      taskViewModel.checkIfEmpty.observe(viewLifecycleOwner){
         when(it){
            0 -> {
               binding.imageView.visibility = View.VISIBLE
               binding.textSwipe.visibility = View.VISIBLE
            }
            else -> {
               binding.imageView.visibility = View.GONE
               binding.textSwipe.visibility = View.GONE
            }
         }
      }
   }

   @SuppressLint("SetTextI18n")
   private fun showUpdateSheet(tasks: Task, title : String, button : String){
      val inputSheet = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
      val view = layoutInflater.inflate(R.layout.fragment_task, null)
      inputSheet.setContentView(view)
      inputSheet.show()

      val layoutTask = view.findViewById<TextInputLayout>(R.id.layout_task)
      val inputTask = view.findViewById<TextInputEditText>(R.id.input_task)
      val inputSubject = view.findViewById<TextInputEditText>(R.id.input_subject)
      val buttonSubmit = view.findViewById<Button>(R.id.button_submit)
      val headerTitle = view.findViewById<TextView>(R.id.header_title)
      val radioCategories = view.findViewById<RadioGroup>(R.id.radio_categories)

      inputTask.setText(tasks.task)
      inputSubject.setText(tasks.subject)
      headerTitle.text = title
      buttonSubmit.text = button

      when(tasks.priority){
         Constant.mImportant -> view.findViewById<RadioButton>(R.id.button_important).isChecked = true
         Constant.mNeedToBeDone -> view.findViewById<RadioButton>(R.id.button_medium).isChecked = true
         Constant.mCanDoItAnytime -> view.findViewById<RadioButton>(R.id.button_less).isChecked = true
      }

      buttonSubmit.setOnClickListener {
         val task = inputTask.text.toString()
         val sub = inputSubject.text.toString()
         var c = "categories"
         when(radioCategories.checkedRadioButtonId){
            R.id.button_important -> c = Constant.mImportant
            R.id.button_medium -> c = Constant.mNeedToBeDone
            R.id.button_less -> c = Constant.mCanDoItAnytime
         }
         when {
            task.isNotEmpty() -> {
               val task = Task(tasks.id, task, sub, c, Constant.mFalse)
               taskViewModel.insertTask(task)
               inputSheet.dismiss()
            }
            else -> {
               layoutTask.error = getString(R.string.task_empty_error)
               inputTask.background = AppCompatResources.getDrawable(requireContext(), R.drawable.box_background_error)
            }
         }
      }
   }

   private fun setupRecyclerView(taskList : List<Task>){
      val rv = binding.rvTask
      val rvAdapter = TaskAdapter(arrayListOf(), object : OnItemClickListener {
         override fun onCheckListener(task: Task) {
            taskViewModel.updateTask(Task(task.id, task.task, task.subject, task.priority, Constant.mTrue))
            Toast.makeText(context, "${task.task} is checked", Toast.LENGTH_SHORT).show()
         }

         override fun onUpdateClickListener(task: Task) {
            showUpdateSheet(task, getString(R.string.update_title), getString(R.string.update_button))
         }

      })
      rvAdapter.updateList(taskList)
      rv.apply {
         adapter = rvAdapter
         layoutManager = LinearLayoutManager(context)
         setHasFixedSize(true)
      }
   }

   private fun setAppLocale(context: Context, language: String) {
      val locale = Locale(language)
      Locale.setDefault(locale)
      val config = context.resources.configuration
      config.setLocale(locale)
      context.createConfigurationContext(config)
      context.resources.updateConfiguration(config, context.resources.displayMetrics)
   }

   private fun onBackPressed(){
      requireActivity().onBackPressedDispatcher.addCallback {
         Toast.makeText(context, "use home button to exit the app", Toast.LENGTH_SHORT).show()
      }
   }

   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
   }
}