package com.sendiko.justdoit.ui.register

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.sendiko.justdoit.R
import com.sendiko.justdoit.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

   private var _binding : FragmentRegisterBinding?= null
   private val binding get() = _binding!!

   private val registerViewModel : RegisterViewModel by activityViewModels()

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {
      _binding = FragmentRegisterBinding.inflate(layoutInflater)
      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)

      binding.loginHere.setOnClickListener {
         toLoginFragment()
      }

      binding.buttonRegister.setOnClickListener {
         // TODO : VALIDATION AND NAVIGATE TO LOGIN FRAGMENT
      }

      registerViewModel.isFailed.observe(viewLifecycleOwner){
         when (it.isFailed) {
            true -> showSnackbar(it.errorMessage.toString())
            else -> null
         }
      }

      registerViewModel.isLoading.observe(viewLifecycleOwner){
         when{
            it -> binding.progressBar3.isVisible = true
            else -> binding.progressBar3.isVisible = false
         }
      }

      binding.buttonRegister.setOnClickListener {
         val u = binding.inputUsername.text.toString()
         val e = binding.inputEmail.text.toString()
         val p = binding.inpurPassword.toString()
         val cp = binding.inputConfirmPassword.toString()
      }

   }

   private fun toLoginFragment(){
      val fadeOut = AnimationUtils.loadAnimation(context, R.anim.fade_out)
      binding.textView2.startAnimation(fadeOut)
      binding.textview3.startAnimation(fadeOut)
      binding.labelUsername.startAnimation(fadeOut)
      binding.layoutUsername.startAnimation(fadeOut)
      binding.labelEmail.startAnimation(fadeOut)
      binding.layoutEmail.startAnimation(fadeOut)
      binding.labelPassword.startAnimation(fadeOut)
      binding.layoutPassword.startAnimation(fadeOut)
      binding.labelConfirmPassword.startAnimation(fadeOut)
      binding.layoutConfirm.startAnimation(fadeOut)
      binding.buttonRegister.startAnimation(fadeOut)
      binding.loginHere.startAnimation(fadeOut)
      Handler(Looper.getMainLooper()).postDelayed({
         binding.textView2.visibility = View.INVISIBLE
         binding.textview3.visibility = View.INVISIBLE
         binding.labelUsername.visibility = View.INVISIBLE
         binding.layoutUsername.visibility = View.INVISIBLE
         binding.labelEmail.visibility = View.INVISIBLE
         binding.layoutEmail.visibility = View.INVISIBLE
         binding.labelPassword.visibility = View.INVISIBLE
         binding.layoutPassword.visibility = View.INVISIBLE
         binding.labelConfirmPassword.visibility = View.INVISIBLE
         binding.layoutConfirm.visibility = View.INVISIBLE
         binding.buttonRegister.visibility = View.INVISIBLE
         binding.loginHere.visibility = View.INVISIBLE
         findNavController().navigate(R.id.action_registerFragment2_to_loginFragment2)
      }, 350)
   }

   private fun showSnackbar(message : String){
      Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
   }

   private fun validation(u: String, e : String, p: String, cp : String): Boolean {
      var valid: Boolean
      when{
         u.isEmpty() -> {
            binding.layoutUsername.error = "Username can't be empty"
            valid = false
         }
         e.isEmpty() -> {
            binding.layoutEmail.error = "Email can't be empty"
            valid = false
         }
         p.isEmpty() -> {
            binding.layoutPassword.error = "Password can't be empty"
            valid = false
         }
         p.length < 8 -> {
            binding.layoutPassword.error = "Password need to be at least 8 characters"
            valid = false
         }
         p != cp -> {
            valid = false
            binding.layoutPassword.error = "Password didn't match"
            binding.layoutConfirm.error = "Password didn't match"
         }
         else -> {
            binding.layoutUsername.error = null
            binding.layoutEmail.error = null
            binding.layoutPassword.error = null
            binding.layoutConfirm.error = null
            valid = true
         }
      }
      return valid
   }

   override fun onDestroy() {
      super.onDestroy()
      _binding = null
   }

}