package com.sendiko.justdoit.ui.splash

import android.annotation.SuppressLint
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
import androidx.navigation.fragment.findNavController
import com.sendiko.justdoit.R
import com.sendiko.justdoit.databinding.FragmentSplashScreenBinding
import com.sendiko.justdoit.repository.SharedViewModel
import com.sendiko.justdoit.repository.preferences.AuthPreferences
import com.sendiko.justdoit.repository.preferences.AuthViewModel
import com.sendiko.justdoit.repository.preferences.AuthViewModelFactory
import com.sendiko.justdoit.repository.preferences.SettingsPreference
import com.sendiko.justdoit.ui.container.MainActivity
import com.sendiko.justdoit.ui.container.dataStore1
import com.sendiko.justdoit.ui.settings.SettingsViewModel
import java.util.*

private const val TAG = "SplashScreenFragment"
@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : Fragment() {

   private var _binding : FragmentSplashScreenBinding?= null
   private val binding get() = _binding!!

   val sharedViewModel : SharedViewModel by activityViewModels()

   private val pref by lazy{
      val context = requireNotNull(this.context)
      AuthPreferences.getInstance(context.dataStore1)
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
      _binding = FragmentSplashScreenBinding.inflate(layoutInflater)

      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      val zoomOut = AnimationUtils.loadAnimation(context, R.anim.zoom_out)
      val zoomIn = AnimationUtils.loadAnimation(context, R.anim.zoom_in)
      binding.appName.startAnimation(zoomOut)
      binding.appLogo.startAnimation(zoomOut)

      settingsViewModel.getLanguage().observe(viewLifecycleOwner){
         setAppLocale(requireContext(), it)
      }

      Handler(Looper.myLooper()!!).postDelayed({
         binding.appName.startAnimation(zoomIn)
         binding.appLogo.startAnimation(zoomIn)
      }, 100)

      authViewModel.getLoginState().observe(viewLifecycleOwner){ isLoggedIn ->
         when(isLoggedIn){
            true -> {
               Handler(Looper.getMainLooper()).postDelayed({
                  startActivity(Intent(requireActivity(), MainActivity::class.java))
               }, 500)
            }
            else -> {
               Handler(Looper.getMainLooper()).postDelayed({
                  findNavController().navigate(R.id.action_splashScreenFragment_to_loginFragment)
               }, 1000)
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

}