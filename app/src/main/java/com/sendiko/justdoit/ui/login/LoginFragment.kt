package com.sendiko.justdoit.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.sendiko.justdoit.R
import com.sendiko.justdoit.databinding.FragmentLoginBinding
import com.sendiko.justdoit.repository.SharedViewModel
import com.sendiko.justdoit.repository.preferences.AuthPreferences
import com.sendiko.justdoit.repository.preferences.AuthViewModel
import com.sendiko.justdoit.repository.preferences.AuthViewModelFactory
import com.sendiko.justdoit.repository.preferences.SettingsPreference
import com.sendiko.justdoit.ui.container.MainActivity
import com.sendiko.justdoit.ui.container.dataStore1
import com.sendiko.justdoit.ui.settings.SettingsViewModel
import java.util.*

class LoginFragment : Fragment() {

   private var _binding : FragmentLoginBinding?= null
   private val binding get() = _binding!!

   private val sharedViewModel : SharedViewModel by activityViewModels()

   private val pref by lazy {
      AuthPreferences.getInstance(requireNotNull(this.context).dataStore1)
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
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {
      _binding = FragmentLoginBinding.inflate(layoutInflater)
      toThisFragment()
      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      val fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in)
      binding.root.startAnimation(fadeIn)

      settingsViewModel.getLanguage().observe(viewLifecycleOwner){
         setAppLocale(requireContext(), it)
      }

      binding.buttonLogin.setOnClickListener {
         val u = binding.inputUsername.text.toString()
         val p = binding.inputUsername.text.toString()
         when{
            validation(u, p) -> postLogin(u)
         }
      }

   }

   private fun toThisFragment(){
      val fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in)
      binding.labelUsername.startAnimation(fadeIn)
      binding.layoutUsername.startAnimation(fadeIn)
      binding.layoutToolbar.startAnimation(fadeIn)
      binding.buttonLogin.startAnimation(fadeIn)
      Handler(Looper.getMainLooper()).postDelayed({
         binding.labelUsername.visibility = View.VISIBLE
         binding.layoutUsername.visibility = View.VISIBLE
         binding.layoutToolbar.visibility = View.VISIBLE
         binding.buttonLogin.visibility = View.VISIBLE
      }, 2000)
   }

   private fun showSnackbar(message : String){
      Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
   }

   private fun postLogin(u : String){
      authViewModel.saveUsername(u)
      authViewModel.setLoginState(true)
      sharedViewModel.saveUsername(u)
      val intent = Intent(requireActivity(), MainActivity::class.java)
      startActivity(intent)
   }

   private fun validation(username : String, password : String): Boolean {
      var valid = true
      when {
         username.isNotBlank() && password.isNotBlank() -> {
            valid = true
            binding.layoutUsername.error = null
         }
         username.isEmpty() -> {
            valid = false
            binding.layoutUsername.error = "Username can't be empty"
         }
      }
      return valid
   }

   private fun setAppLocale(context: Context, language: String) {
      val locale = Locale(language)
      Locale.setDefault(locale)
      val config = context.resources.configuration
      config.setLocale(locale)
      context.createConfigurationContext(config)
      context.resources.updateConfiguration(config, context.resources.displayMetrics)
   }

   override fun onDestroy() {
      super.onDestroy()
      _binding = null
   }

}