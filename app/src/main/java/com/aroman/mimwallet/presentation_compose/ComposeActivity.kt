package com.aroman.mimwallet.presentation_compose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.aroman.mimwallet.presentation_compose.ui.navigation.Navigation
import com.aroman.mimwallet.presentation_compose.ui.viewmodels.ComposeThemeViewModel
import com.aroman.mimwallet.presentation_compose.ui.viewmodels.ComposeWalletViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComposeActivity : AppCompatActivity() {
    private val walletViewModel by viewModels<ComposeWalletViewModel>()
    private val themeViewModel by viewModels<ComposeThemeViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Navigation(walletViewModel = walletViewModel, themeViewModel = themeViewModel)
        }
    }
}