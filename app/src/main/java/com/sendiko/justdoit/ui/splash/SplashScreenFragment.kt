package com.sendiko.justdoit.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sendiko.justdoit.R
import com.sendiko.justdoit.databinding.FragmentSplashScreenBinding

class SplashScreenFragment : Fragment() {

    private var _binding : FragmentSplashScreenBinding?= null
    private val binding get() = _binding!!

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

        Handler(Looper.myLooper()!!).postDelayed({
            findNavController().navigate(R.id.action_splashScreenFragment_to_loginFragment)
        }, 1000)

    }
}