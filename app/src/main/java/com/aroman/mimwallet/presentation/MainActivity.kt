package com.aroman.mimwallet.presentation

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.LayoutInflaterCompat
import com.aroman.mimwallet.R
import com.aroman.mimwallet.databinding.ActivityMainBinding
import com.aroman.mimwallet.presentation.wallet.WalletFragment
import com.aroman.mimwallet.utils.theming.DynamicLayoutInflater
import com.aroman.mimwallet.utils.theming.ThemeManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        LayoutInflaterCompat.setFactory2(
            LayoutInflater.from(this),
            DynamicLayoutInflater(delegate)
        )
        checkPrefsForTheme()

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun checkPrefsForTheme() {
        when (getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE)
            .getInt(WalletFragment.NIGHT_MODE, 0)) {
            0 -> {
                ThemeManager.theme = ThemeManager.Theme.LIGHT
            }
            else -> {
                ThemeManager.theme = ThemeManager.Theme.DARK
            }
        }
    }
}