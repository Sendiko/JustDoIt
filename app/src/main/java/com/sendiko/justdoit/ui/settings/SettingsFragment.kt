package com.sendiko.justdoit.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sendiko.justdoit.FirstActivity
import com.sendiko.justdoit.R
import com.sendiko.justdoit.dataStore
import com.sendiko.justdoit.databinding.FragmentSettingsBinding
import com.sendiko.justdoit.repository.preferences.AuthPreferences
import com.sendiko.justdoit.repository.preferences.AuthViewModel
import com.sendiko.justdoit.repository.preferences.AuthViewModelFactory

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val pref by lazy{
        val context = requireNotNull(this.context)
        AuthPreferences.getInstance(context.dataStore)
    }

    private val authViewModel : AuthViewModel by lazy {
        ViewModelProvider(this, AuthViewModelFactory(pref))[AuthViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val settingsViewModel =
            ViewModelProvider(this)[SettingsViewModel::class.java]

        _binding = FragmentSettingsBinding.inflate(layoutInflater)
        val root: View = binding.root

        binding.icCancel.setOnClickListener {
            findNavController().navigate(R.id.action_navigation_notifications_to_navigation_home)
        }

        authViewModel.getUser().observe(viewLifecycleOwner){
            binding.greeting3.text = it
        }

        binding.icLogout.setOnClickListener {
            authViewModel.setLoginState(false)
            val intent = Intent(requireActivity(), FirstActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}