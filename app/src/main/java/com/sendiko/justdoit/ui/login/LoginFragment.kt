package com.sendiko.justdoit.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.sendiko.justdoit.MainActivity
import com.sendiko.justdoit.R
import com.sendiko.justdoit.dataStore1
import com.sendiko.justdoit.databinding.FragmentLoginBinding
import com.sendiko.justdoit.repository.SharedViewModel
import com.sendiko.justdoit.repository.preferences.AuthPreferences
import com.sendiko.justdoit.repository.preferences.AuthViewModel
import com.sendiko.justdoit.repository.preferences.AuthViewModelFactory

class LoginFragment : Fragment() {

   private var _binding : FragmentLoginBinding?= null
   private val binding get() = _binding!!

   private val loginViewModel : LoginViewModel by activityViewModels()

   private val pref by lazy{
      val context = requireNotNull(this.context)
      AuthPreferences.getInstance(context.dataStore1)
   }

   private val authViewModel : AuthViewModel by lazy {
      ViewModelProvider(this, AuthViewModelFactory(pref))[AuthViewModel::class.java]
   }

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {
      _binding = FragmentLoginBinding.inflate(layoutInflater)
      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      binding.buttonLogin.setOnClickListener {
         val u = binding.inputUsername.text.toString()
         val p = binding.inputUsername.text.toString()
         when{
            validation(u, p) -> postLogin(u, p)
         }
      }

      binding.textView.setOnClickListener {
         findNavController().navigate(R.id.action_loginFragment2_to_registerFragment2)
      }

      loginViewModel.isFailed.observe(viewLifecycleOwner){
         when(it.isFailed){
            true -> showSnackbar(it.errorMessage.toString())
            else -> null
         }
      }

   }

   private fun showSnackbar(message : String){
      Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
   }

   private fun postLogin(u: String, p: String) {

      loginViewModel.loginWithUser(u, p).observe(viewLifecycleOwner){
         authViewModel.saveUsername(u)
         authViewModel.setLoginState(true)
         val intent = Intent(requireActivity(), MainActivity::class.java)
         startActivity(intent)
      }
   }

   private fun validation(username : String, password : String): Boolean {
      var valid = true
      when {
         username.isNotBlank() -> {
            when {
               password.isNotBlank() -> {
                  valid = true
                  binding.layoutUsername.error = null
                  binding.layoutPassword.error = null
               }
               username.isBlank() || password.isBlank() -> {
                  valid = false
                  binding.layoutUsername.error = "Please fill in Username and Password"
                  binding.layoutPassword.error = "Please fill in Username and Password"
               }
               password.length < 8 -> {
                  binding.layoutPassword.error = "Password must be at least 8 characters"
                  valid = false
               }
            }
         }
         username.isBlank() || password.isBlank() -> {
            valid = false
            binding.layoutUsername.error = "Please fill in Username and Password"
            binding.layoutPassword.error = "Please fill in Username and Password"
         }
         password.length < 8 -> {
            binding.layoutPassword.error = "Password must be at least 8 characters"
            valid = false
         }
      }
      return valid
   }

   override fun onDestroy() {
      super.onDestroy()
      _binding = null
   }

}