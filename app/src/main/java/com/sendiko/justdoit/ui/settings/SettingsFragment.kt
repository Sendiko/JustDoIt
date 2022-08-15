package com.sendiko.justdoit.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.sendiko.justdoit.databinding.FragmentSettingsBinding
import com.sendiko.justdoit.repository.SharedViewModel
import com.sendiko.justdoit.repository.preferences.AuthPreferences
import com.sendiko.justdoit.repository.preferences.AuthViewModel
import com.sendiko.justdoit.repository.preferences.AuthViewModelFactory
import com.sendiko.justdoit.ui.container.FirstActivity
import com.sendiko.justdoit.ui.container.MainActivity
import com.sendiko.justdoit.ui.container.dataStore3

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

      binding.icCancel.setOnClickListener {
         val intent = Intent(requireActivity(), MainActivity::class.java)
         requireActivity().navigateUpTo(intent)
      }

      authViewModel.getUser().observe(viewLifecycleOwner){
         binding.greeting3.text = it
      }

      binding.icLogout.setOnClickListener {
         authViewModel.setLoginState(false)
         authViewModel.clearUser()
         sharedViewModel.removeUsername()
         val intent = Intent(requireActivity(), FirstActivity::class.java)
         startActivity(intent)
      }

   }

   override fun onDestroyView() {
      super.onDestroyView()
      _binding = null
   }

}