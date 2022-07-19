package com.sendiko.justdoit.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sendiko.justdoit.R
import com.sendiko.justdoit.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding?= null
    private val binding get() = _binding!!

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
                validation(u, p) -> findNavController().navigate(R.id.action_loginFragment_to_navigation_home)
            }
        }
        binding.textView.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
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