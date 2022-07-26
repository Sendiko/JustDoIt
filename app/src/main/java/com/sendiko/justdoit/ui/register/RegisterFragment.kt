package com.sendiko.justdoit.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
         findNavController().navigate(R.id.action_registerFragment2_to_loginFragment2)
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
         when{
            validation(u, e, p, cp) -> registerUser(e, p)
         }
      }

   }

   private fun showSnackbar(message : String){
      Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
   }

   private fun registerUser(e : String, p: String){
      registerViewModel.registerUser(e, p).observe(viewLifecycleOwner){
         when{
            it -> findNavController().navigate(R.id.action_registerFragment2_to_loginFragment2)
         }
      }
   }

   private fun validation(u: String, e : String, p: String, cp : String): Boolean {
      var valid = true
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
         else -> valid = true
      }
      return valid
   }

   override fun onDestroy() {
      super.onDestroy()
      _binding = null
   }

}