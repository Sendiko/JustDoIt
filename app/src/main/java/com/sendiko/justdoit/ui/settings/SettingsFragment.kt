package com.sendiko.justdoit.ui.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.sendiko.justdoit.R
import com.sendiko.justdoit.databinding.FragmentSettingsBinding
import com.sendiko.justdoit.repository.SharedViewModel
import com.sendiko.justdoit.repository.preferences.AuthPreferences
import com.sendiko.justdoit.repository.preferences.AuthViewModel
import com.sendiko.justdoit.repository.preferences.AuthViewModelFactory
import com.sendiko.justdoit.repository.preferences.SettingsPreference
import com.sendiko.justdoit.ui.container.FirstActivity
import com.sendiko.justdoit.ui.container.MainActivity
import com.sendiko.justdoit.ui.container.dataStore1
import com.sendiko.justdoit.ui.container.dataStore3
import com.sendiko.justdoit.ui.settings.SettingsViewModel.SettingsViewModelFactory
import java.util.*

class SettingsFragment : Fragment() {

   private var _binding: FragmentSettingsBinding? = null
   private val binding get() = _binding!!

   private val sharedViewModel : SharedViewModel by activityViewModels()

   private val pref by lazy{
      val context = requireNotNull(this.context)
      AuthPreferences.getInstance(context.dataStore3)
   }

   private val authViewModel : AuthViewModel by lazy {
      ViewModelProvider(this, AuthViewModelFactory(pref))[AuthViewModel::class.java]
   }

   private val settingsPref by lazy {
      SettingsPreference.getInstance(requireNotNull(this.context).dataStore1)
   }

   private val settingsViewModel : SettingsViewModel by lazy {
      ViewModelProvider(this, SettingsViewModelFactory(settingsPref))[SettingsViewModel::class.java]
   }

   override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {
      _binding = FragmentSettingsBinding.inflate(layoutInflater)
      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      settingsViewModel.getLanguage().observe(viewLifecycleOwner){
         setAppLocale(requireContext(), it)
      }

      binding.icCancel.setOnClickListener {
         requireActivity().navigateUpTo(Intent(requireActivity(), MainActivity::class.java))
      }

      authViewModel.getUser().observe(viewLifecycleOwner){
         binding.greeting3.text = it
      }

      binding.frameLogout.setOnClickListener {
         authViewModel.setLoginState(false)
         authViewModel.clearUser()
         startActivity(Intent(requireActivity(), FirstActivity::class.java))
      }

      binding.frameContacts.setOnClickListener {
         val link = "https://github.com/Sendiko/JustDoIt/issues"
         link.openLink()
      }

      binding.frameLanguages.setOnClickListener {
         showLanguageDialog()
      }

      binding.frameDisplayMode.setOnClickListener {
         showDisplayDialog()
      }

      binding.frameChangeName.setOnClickListener {
         showChangeNameDialog()
      }

   }

   private fun showLanguageDialog(){
      val languageDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
      val view = layoutInflater.inflate(R.layout.fragment_language_dialog, null)
      languageDialog.setContentView(view)
      languageDialog.show()

      val radioGroup = view.findViewById<RadioGroup>(R.id.languages_checkbox)
      val buttonSubmit = view.findViewById<Button>(R.id.button_submit)
      buttonSubmit.setOnClickListener {
         when(radioGroup.checkedRadioButtonId){
            R.id.language_english -> settingsViewModel.setLanguage("en")
            R.id.language_indonesian -> settingsViewModel.setLanguage("id")
         }
         languageDialog.dismiss()
         startActivity(Intent(requireActivity(), FirstActivity::class.java))
         requireActivity().overridePendingTransition(R.anim.activity_fade_out, R.anim.activity_fade_in)
      }

      settingsViewModel.getLanguage().observe(viewLifecycleOwner){
         when(it){
            "en" -> view.findViewById<RadioButton>(R.id.language_english).isChecked = true
            "id" -> view.findViewById<RadioButton>(R.id.language_indonesian).isChecked = true
         }
      }

   }

   private fun showDisplayDialog(){
      val displayDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
      val view = layoutInflater.inflate(R.layout.fragment_display_dialog, null)
      displayDialog.setContentView(view)
      displayDialog.show()

      val isItNightMode = when {
          Build.VERSION.SDK_INT >= Build.VERSION_CODES.R -> {
             requireContext().resources.configuration.isNightModeActive
          }
         else -> null
      }

      when(isItNightMode){
         true ->  view.findViewById<RadioButton>(R.id.theme_dark).isChecked = true
         else -> view.findViewById<RadioButton>(R.id.theme_light).isChecked = true
      }

      settingsViewModel.getDarkTheme().observe(viewLifecycleOwner){
         when(it){
            true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
         }
      }

      val radioGroup = view.findViewById<RadioGroup>(R.id.display_checkbox)
      view.findViewById<Button>(R.id.button_submit).setOnClickListener {
         when(radioGroup.checkedRadioButtonId){
            R.id.theme_light -> settingsViewModel.setDarkTheme(false)
            R.id.theme_dark -> settingsViewModel.setDarkTheme(true)
         }
         displayDialog.dismiss()
      }
   }

   private fun showChangeNameDialog(){
      val changeNameDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
      val view = layoutInflater.inflate(R.layout.fragment_change_name_dialog, null)
      changeNameDialog.setContentView(view)
      changeNameDialog.show()

      val layoutName = view.findViewById<TextInputLayout>(R.id.layout_name)
      val inputName = view.findViewById<TextInputEditText>(R.id.input_name)

      authViewModel.getUser().observe(viewLifecycleOwner){
         inputName.setText(it)
      }

      view.findViewById<Button>(R.id.button_submit).setOnClickListener {
         when{
            inputName.text.isNullOrEmpty() -> {
               inputName?.background = AppCompatResources.getDrawable(requireContext(), R.drawable.box_background_error)
               layoutName.error = getString(R.string.nama_error_empty)
            }
            else -> {
               authViewModel.clearUser()
               authViewModel.saveUsername(inputName.text.toString())
               changeNameDialog.dismiss()
            }
         }
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

   private fun String.openLink() {
      val intent = Intent(Intent.ACTION_VIEW)
      intent.data = Uri.parse(this)
      requireActivity().startActivity(intent)
   }

   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
   }

}