package com.aroman.mimwallet.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aroman.mimwallet.R
import com.aroman.mimwallet.presentation.coin_list.CoinListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onStart() {
        super.onStart()
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, CoinListFragment())
            .addToBackStack(null)
            .commit()
    }
}