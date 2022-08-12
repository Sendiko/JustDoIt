package com.sendiko.justdoit.ui.login

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
import com.google.android.material.snackbar.Snackbar
import com.sendiko.justdoit.R
import com.sendiko.justdoit.databinding.FragmentLoginBinding
import com.sendiko.justdoit.repository.preferences.AuthPreferences
import com.sendiko.justdoit.repository.preferences.AuthViewModel
import com.sendiko.justdoit.repository.preferences.AuthViewModelFactory
import com.sendiko.justdoit.ui.container.MainActivity
import com.sendiko.justdoit.ui.container.dataStore1

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
      toThisFragment()
      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      val fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in)
      binding.root.startAnimation(fadeIn)

      binding.buttonLogin.setOnClickListener {
         val u = binding.inputUsername.text.toString()
         val p = binding.inputUsername.text.toString()
         when{
            validation(u, p) -> postLogin(u)
         }
      }

      binding.textView.setOnClickListener {
         toRegisterFragment()
      }

      loginViewModel.isFailed.observe(viewLifecycleOwner){
         when(it.isFailed){
            true -> showSnackbar(it.errorMessage.toString())
            else -> null
         }
      }

   }

   private fun toRegisterFragment(){
      val fadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out)
      binding.labelUsername.startAnimation(fadeOut)
      binding.layoutUsername.startAnimation(fadeOut)
      binding.labelPassword.startAnimation(fadeOut)
      binding.layoutPassword.startAnimation(fadeOut)
      binding.layoutToolbar.startAnimation(fadeOut)
      binding.buttonLogin.startAnimation(fadeOut)
      binding.textView.startAnimation(fadeOut)
      Handler(Looper.getMainLooper()).postDelayed({
         binding.labelUsername.visibility = View.INVISIBLE
         binding.layoutUsername.visibility = View.INVISIBLE
         binding.labelPassword.visibility = View.INVISIBLE
         binding.layoutPassword.visibility = View.INVISIBLE
         binding.layoutToolbar.visibility = View.INVISIBLE
         binding.buttonLogin.visibility = View.INVISIBLE
         binding.textView.visibility = View.INVISIBLE
         findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
      }, 350)
   }

   private fun toThisFragment(){
      val fadeIn = AnimationUtils.loadAnimation(context, R.anim.fade_in)
      binding.labelUsername.startAnimation(fadeIn)
      binding.layoutUsername.startAnimation(fadeIn)
      binding.labelPassword.startAnimation(fadeIn)
      binding.layoutPassword.startAnimation(fadeIn)
      binding.layoutToolbar.startAnimation(fadeIn)
      binding.buttonLogin.startAnimation(fadeIn)
      binding.textView.startAnimation(fadeIn)
      Handler(Looper.getMainLooper()).postDelayed({
         binding.labelUsername.visibility = View.VISIBLE
         binding.layoutUsername.visibility = View.VISIBLE
         binding.labelPassword.visibility = View.VISIBLE
         binding.layoutPassword.visibility = View.VISIBLE
         binding.layoutToolbar.visibility = View.VISIBLE
         binding.buttonLogin.visibility = View.VISIBLE
         binding.textView.visibility = View.VISIBLE
      }, 2000)
   }

   private fun showSnackbar(message : String){
      Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
   }

   private fun postLogin(u : String){
      authViewModel.saveUsername(u)
      authViewModel.setLoginState(true)
      val intent = Intent(requireActivity(), MainActivity::class.java)
      startActivity(intent)
   }

   private fun validation(username : String, password : String): Boolean {
      var valid = true
      when {
         username.isNotBlank() && password.isNotBlank() -> {
            valid = true
            binding.layoutUsername.error = null
            binding.layoutPassword.error = null
         }
         username.isEmpty() -> {
            valid = false
            binding.layoutUsername.error = "Username can't be empty"
         }
         password.isBlank() -> {
            valid = false
            binding.layoutPassword.error = "Password can't be empty"
         }
         password.length < 8 -> {
            valid = false
            binding.layoutPassword.error = "Password must be at least 8 characters"
         }
      }
      return valid
   }

   override fun onDestroy() {
      super.onDestroy()
      _binding = null
   }

}