package com.example.lostfound.presentation.activity

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.lostfound.R
import com.example.lostfound.databinding.ActivityMainBinding
import com.example.lostfound.presentation.base.BaseActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //linking bottom navigation with navController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        bottomNavigation.setupWithNavController(navController)

        //logic to hide navigation in register and login fragments
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.logInFragment || destination.id == R.id.registrationFragment) {
                bottomNavigation.visibility = android.view.View.GONE
            } else {
                bottomNavigation.visibility = android.view.View.VISIBLE
            }
        }

    }
}