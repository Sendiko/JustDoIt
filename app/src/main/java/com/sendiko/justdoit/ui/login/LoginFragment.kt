package com.sendiko.justdoit.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sendiko.justdoit.R
import com.sendiko.justdoit.dataStore
import com.sendiko.justdoit.databinding.FragmentLoginBinding
import com.sendiko.justdoit.repository.preferences.AuthPreferences
import com.sendiko.justdoit.repository.preferences.AuthViewModel
import com.sendiko.justdoit.repository.preferences.AuthViewModelFactory

class LoginFragment : Fragment() {

    private var _binding : FragmentLoginBinding?= null
    private val binding get() = _binding!!

    private val pref by lazy{
        val context = requireNotNull(this.context)
        AuthPreferences.getInstance(context.dataStore)
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
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun postLogin(u: String, p: String) {
        authViewModel.setLoginState(true)
        findNavController().navigate(R.id.action_loginFragment_to_navigation_home)
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