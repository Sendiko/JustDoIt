package com.sendiko.justdoit

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sendiko.justdoit.databinding.ActivityMainBinding

val Context.dataStore : DataStore<Preferences> by preferencesDataStore(name = "preferences")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        navView.setupWithNavController(navController)
        navView.itemActiveIndicatorColor = null

        navController.addOnDestinationChangedListener{ _, destination, _, ->
            when(destination.id){
                R.id.navigation_notifications -> navView.isVisible = false
                else -> navView.isVisible = true
            }
        }

    }
}