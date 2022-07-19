package com.sendiko.justdoit.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.sendiko.justdoit.R
import com.sendiko.justdoit.dataStore
import com.sendiko.justdoit.databinding.FragmentSplashScreenBinding
import com.sendiko.justdoit.repository.preferences.AuthPreferences
import com.sendiko.justdoit.repository.preferences.AuthViewModel
import com.sendiko.justdoit.repository.preferences.AuthViewModelFactory

class SplashScreenFragment : Fragment() {

    private var _binding : FragmentSplashScreenBinding?= null
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
        _binding = FragmentSplashScreenBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val zoomOut = AnimationUtils.loadAnimation(context, R.anim.zoom_out)
        val zoomIn = AnimationUtils.loadAnimation(context, R.anim.zoom_in)
        binding.appName.startAnimation(zoomOut)
        binding.appLogo.startAnimation(zoomOut)

        Handler(Looper.myLooper()!!).postDelayed({
            binding.appName.startAnimation(zoomIn)
            binding.appLogo.startAnimation(zoomIn)
        }, 100)

        authViewModel.getLoginState().observe(viewLifecycleOwner){ isLoggedIn ->
            when(isLoggedIn){
                true -> {
                    Handler(Looper.getMainLooper()).postDelayed({
                        findNavController().navigate(R.id.action_splashScreenFragment_to_navigation_home)
                    }, 1000)
                }
                else -> {
                    Handler(Looper.getMainLooper()).postDelayed({
                        findNavController().navigate(R.id.action_splashScreenFragment_to_loginFragment)
                    }, 1000)
                }
            }
        }

    }
}