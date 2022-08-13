package com.sendiko.justdoit.ui.container

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.sendiko.justdoit.R
import com.sendiko.justdoit.databinding.ActivityMainBinding
import com.sendiko.justdoit.repository.ViewModelFactory
import com.sendiko.justdoit.repository.model.Task
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

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)

      binding = ActivityMainBinding.inflate(layoutInflater)
      setContentView(binding.root)

      val navView: BottomNavigationView = binding.navView
      val navController = findNavController(R.id.nav_host_fragment_activity_main)

      val fadeOut = AnimationUtils.loadAnimation(this,R.anim.fade_out)

      navView.setupWithNavController(navController)
      navView.itemActiveIndicatorColor = null
      navView.background = null
      navView.menu.getItem(1).isEnabled = false

      binding.buttonAddTask.setOnClickListener {
         showInputSheet()
      }

      navController.addOnDestinationChangedListener{ _, destination, _, ->
         when(destination.id){
            R.id.navigation_task -> {
               navView.startAnimation(fadeOut)
               binding.bottomAppBar.isVisible = false
            }
            else -> navView.isVisible = true
         }
      }

   }

   private fun showInputSheet(){
      val inputSheet = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
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
         val task = Task(t, s, "false")
         taskViewModel.insertTask(task)
         inputSheet.dismiss()
      }

   }

}