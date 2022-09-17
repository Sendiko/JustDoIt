   package com.sendiko.justdoit.ui.container

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.sendiko.justdoit.R
import com.sendiko.justdoit.databinding.ActivityMainBinding
import com.sendiko.justdoit.repository.Constant
import com.sendiko.justdoit.repository.ViewModelFactory
import com.sendiko.justdoit.repository.model.Task
import com.sendiko.justdoit.repository.preferences.SettingsPreference
import com.sendiko.justdoit.ui.settings.SettingsViewModel
import com.sendiko.justdoit.ui.task.TaskViewModel

val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "preferences")
class MainActivity : AppCompatActivity() {

   private lateinit var binding: ActivityMainBinding

   private val taskViewModel : TaskViewModel by lazy {
      val activity = requireNotNull(application)
      getViewModel(activity)
   }

   private fun getViewModel(application: Application) : TaskViewModel {
      val factory = ViewModelFactory.getInstance(application)
      return ViewModelProvider(this, factory)[TaskViewModel::class.java]
   }

   private val settingsPref by lazy {
      SettingsPreference.getInstance(requireNotNull(this).dataStore1)
   }

   private val settingsViewModel : SettingsViewModel by lazy {
      ViewModelProvider(this,
         SettingsViewModel.SettingsViewModelFactory(settingsPref)
      )[SettingsViewModel::class.java]
   }

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)

      binding = ActivityMainBinding.inflate(layoutInflater)
      setContentView(binding.root)

      val navView: BottomNavigationView = binding.navView
      val navController = findNavController(R.id.nav_host_fragment_activity_main)

      navView.setupWithNavController(navController)
      navView.itemActiveIndicatorColor = null
      navView.background = null
      navView.menu.getItem(1).isEnabled = false

      binding.buttonAddTask.setOnClickListener {
         showInputDialog()
      }

      binding.imageMore.setOnClickListener {
         showSortListDialog()
      }

   }

   private fun showSortListDialog(){
      val sortListDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
      val view = layoutInflater.inflate(R.layout.fragment_sort_dialog, null)
      sortListDialog.setContentView(view)
      sortListDialog.show()

      val radioCategories = view.findViewById<RadioGroup>(R.id.radio_categories)
      val buttonSubmit = view.findViewById<Button>(R.id.button_submit)
      val clearFilter = view.findViewById<TextView>(R.id.clear_filter)

      settingsViewModel.getSortListKey().observe(this){ sortKey ->
         when(sortKey){
            Constant.mImportant -> {
               view.findViewById<RadioButton>(R.id.button_important).isChecked = true
            }
            Constant.mNeedToBeDone -> {
               view.findViewById<RadioButton>(R.id.button_medium).isChecked = true
            }
            Constant.mCanDoItAnytime -> {
               view.findViewById<RadioButton>(R.id.button_less).isChecked = true
            }
         }
      }

      clearFilter.setOnClickListener {
         settingsViewModel.setSortListKey("none")
         Toast.makeText(this, "Filter cleared", Toast.LENGTH_SHORT).show()
         sortListDialog.dismiss()
      }

      buttonSubmit.setOnClickListener {
         when(radioCategories.checkedRadioButtonId){
            R.id.button_important -> {
               settingsViewModel.setSortListKey(Constant.mImportant)
               Toast.makeText(this, getString(R.string.filter_important), Toast.LENGTH_SHORT).show()
            }
            R.id.button_medium -> {
               settingsViewModel.setSortListKey(Constant.mNeedToBeDone)
               Toast.makeText(this, getString(R.string.filter_need_to_be_done), Toast.LENGTH_SHORT).show()
            }
            R.id.button_less -> {
               settingsViewModel.setSortListKey(Constant.mCanDoItAnytime)
               Toast.makeText(this, getString(R.string.filter_can_do_it_anytime), Toast.LENGTH_SHORT).show()
            }
         }
         sortListDialog.dismiss()
      }

   }

   private fun showInputDialog(){
      val inputDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
      val view = layoutInflater.inflate(R.layout.fragment_task, null)
      inputDialog.setContentView(view)
      inputDialog.show()

      val layoutTask = view.findViewById<TextInputLayout>(R.id.layout_task)
      val inputTask = view.findViewById<TextInputEditText>(R.id.input_task)
      val inputSubject = view.findViewById<TextInputEditText>(R.id.input_subject)
      val buttonSubmit = view.findViewById<Button>(R.id.button_submit)
      val radioCategories = view.findViewById<RadioGroup>(R.id.radio_categories)

      buttonSubmit.setOnClickListener {
         val t = inputTask.text.toString()
         val s = inputSubject.text.toString()
         var c = Constant.category
         when(radioCategories.checkedRadioButtonId){
            R.id.button_important -> c = Constant.mImportant
            R.id.button_medium -> c = Constant.mNeedToBeDone
            R.id.button_less -> c = Constant.mCanDoItAnytime
         }
         when {
            t.isNotEmpty() -> {
               when(c){
                  Constant.category -> Toast.makeText(this, getString(R.string.priority_error), Toast.LENGTH_SHORT).show()
                  else -> {
                     val task = Task(0, t, s, c, Constant.mFalse)
                     taskViewModel.insertTask(task)
                     Toast.makeText(this, "$task", Toast.LENGTH_SHORT).show()
                     inputDialog.dismiss()
                  }
               }
            }
            else -> {
               layoutTask.error = getString(R.string.task_empty_error)
               inputTask.background = AppCompatResources.getDrawable(this, R.drawable.box_background_error)
            }
         }
      }

   }

}