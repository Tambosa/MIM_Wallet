package com.aroman.mimwallet.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aroman.mimwallet.R
import com.aroman.mimwallet.databinding.ActivityMainBinding
import com.aroman.mimwallet.presentation.wallet.WalletFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, WalletFragment())
            .commit()
        initBottomMenu()
    }

    private fun initBottomMenu() {
        binding.navBar.setItemSelected(R.id.menu_portfolio, true)

        binding.navBar.setOnItemSelectedListener { menuId ->
            when (menuId) {
                R.id.menu_portfolio -> {}
                R.id.menu_coins -> {}
                R.id.menu_settings -> {}
            }
        }
    }
}