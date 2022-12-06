package com.aroman.mimwallet.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.aroman.mimwallet.R
import com.aroman.mimwallet.databinding.ActivityMainBinding
import com.aroman.mimwallet.presentation.settings.SettingsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkPrefsForTheme()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        NavigationUI.setupWithNavController(
            binding.navBar,
            Navigation.findNavController(this, R.id.activity_main_nav_host_fragment)
        )
    }

    private fun checkPrefsForTheme() {
        val sharedPreferences = getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE)
        when (sharedPreferences.getInt(SettingsFragment.NIGHT_MODE, 0)) {
            0 -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}