package com.sendiko.justdoit.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sendiko.justdoit.R
import com.sendiko.justdoit.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private var _binding : FragmentRegisterBinding?= null
    private val binding get() = _binding!!

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
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.buttonRegister.setOnClickListener {
            val u = binding.inputUsername.text.toString()
            val e = binding.inputEmail.text.toString()
            val p = binding.inpurPassword.toString()
            val cp = binding.inputConfirmPassword.toString()
            when{
                validation(u, e, p, cp) -> findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
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
            p != cp -> {
                binding.layoutPassword.error = "Password didn't match"
                binding.layoutConfirm.error = "Password didn't match"
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