package com.sendiko.justdoit.ui.container

import android.app.Application
import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.sendiko.justdoit.R
import com.sendiko.justdoit.databinding.ActivityMainBinding
import com.sendiko.justdoit.repository.helper.Constant
import com.sendiko.justdoit.repository.ViewModelFactory
import com.sendiko.justdoit.repository.helper.TaskPriority
import com.sendiko.justdoit.repository.helper.TaskStatus
import com.sendiko.justdoit.repository.model.Task
import com.sendiko.justdoit.repository.preferences.SettingsPreference
import com.sendiko.justdoit.ui.settings.SettingsViewModel
import com.sendiko.justdoit.ui.task.TaskViewModel

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferences")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val taskViewModel: TaskViewModel by lazy {
        val activity = requireNotNull(application)
        getViewModel(activity)
    }

    private fun getViewModel(application: Application): TaskViewModel {
        val factory = ViewModelFactory.getInstance(application)
        return ViewModelProvider(this, factory)[TaskViewModel::class.java]
    }

    private val settingsPref by lazy {
        SettingsPreference.getInstance(requireNotNull(this).dataStore1)
    }

    private val settingsViewModel: SettingsViewModel by lazy {
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

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.navigation_home -> {
                    binding.imageMore.setOnClickListener {
                        showSortListDialog()
                    }
                }
                R.id.navigation_dashboard -> {
                    binding.imageMore.setOnClickListener {
                        deleteAllDialog()
                    }
                }
            }
        }

    }

    private fun deleteAllDialog() {
        val deleteAllDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.fragment_delete_all, null)
        deleteAllDialog.apply {
            setContentView(view)
            show()
        }

        view.findViewById<Button>(R.id.button_cancel).setOnClickListener {
            deleteAllDialog.dismiss()
        }

        view.findViewById<Button>(R.id.button_delete).setOnClickListener {
            taskViewModel.deleteAllTask()
            deleteAllDialog.dismiss()
        }

    }

    private fun showSortListDialog() {
        val sortListDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.fragment_sort_dialog, null)
        sortListDialog.apply {
            setContentView(view)
            show()
        }

        val radioCategories = view.findViewById<RadioGroup>(R.id.radio_categories)
        val buttonSubmit = view.findViewById<Button>(R.id.button_submit)
        val clearFilter = view.findViewById<TextView>(R.id.clear_filter)

        settingsViewModel.getSortListKey().observe(this) { sortKey ->
            when (sortKey) {
                TaskPriority.Important.level -> view.findViewById<RadioButton>(R.id.button_important).isChecked = true
                TaskPriority.NeedToBeDone.level -> view.findViewById<RadioButton>(R.id.button_medium).isChecked = true
                TaskPriority.CanDoItAnytime.level -> view.findViewById<RadioButton>(R.id.button_less).isChecked = true
            }
        }

        clearFilter.setOnClickListener {
            settingsViewModel.setSortListKey("none")
            Toast.makeText(this, "Filter cleared", Toast.LENGTH_SHORT).show()
            sortListDialog.dismiss()
        }

        buttonSubmit.setOnClickListener {
            when (radioCategories.checkedRadioButtonId) {
                R.id.button_important -> {
                    settingsViewModel.setSortListKey(TaskPriority.Important.level)
                    Toast.makeText(this, getString(R.string.filter_important), Toast.LENGTH_SHORT).show()
                }
                R.id.button_medium -> {
                    settingsViewModel.setSortListKey(TaskPriority.NeedToBeDone.level)
                    Toast.makeText(this, getString(R.string.filter_need_to_be_done), Toast.LENGTH_SHORT).show()
                }
                R.id.button_less -> {
                    settingsViewModel.setSortListKey(TaskPriority.CanDoItAnytime.level)
                    Toast.makeText(this, getString(R.string.filter_can_do_it_anytime), Toast.LENGTH_SHORT).show()
                }
            }
            sortListDialog.dismiss()
        }

    }

    private fun showInputDialog() {
        val inputDialog = BottomSheetDialog(this, R.style.BottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.fragment_task, null)
        inputDialog.apply {
            setContentView(view)
            show()
        }

        val layoutTask = view.findViewById<TextInputLayout>(R.id.layout_task)
        val inputTask = view.findViewById<TextInputEditText>(R.id.input_task)
        val inputSubject = view.findViewById<TextInputEditText>(R.id.input_subject)
        val buttonSubmit = view.findViewById<Button>(R.id.button_submit)
        val radioCategories = view.findViewById<RadioGroup>(R.id.radio_categories)

        buttonSubmit.setOnClickListener {
            val t = inputTask.text.toString()
            val s = inputSubject.text.toString()
            var c = Constant.category
            when (radioCategories.checkedRadioButtonId) {
                R.id.button_important -> c = TaskPriority.Important.level
                R.id.button_medium -> c = TaskPriority.NeedToBeDone.level
                R.id.button_less -> c = TaskPriority.CanDoItAnytime.level
            }
            when {
                t.isNotEmpty() -> {
                    when (c) {
                        Constant.category -> Toast.makeText(this, getString(R.string.priority_error), Toast.LENGTH_SHORT).show()
                        else -> {
                            val task = Task(0, t, s, c, TaskStatus.isNotDone.status)
                            taskViewModel.insertTask(task)
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