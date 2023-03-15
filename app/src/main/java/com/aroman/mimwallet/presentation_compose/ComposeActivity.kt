package com.aroman.mimwallet.presentation_compose

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aroman.mimwallet.presentation_compose.ui.coin_insert.CoinInsertScreen
import com.aroman.mimwallet.presentation_compose.ui.portfolio.PortfolioScreen
import com.aroman.mimwallet.presentation_compose.ui.portfolio_notifications.PortfolioNotificationsScreen
import com.aroman.mimwallet.presentation_compose.ui.theme.AppTheme
import com.aroman.mimwallet.presentation_compose.ui.viewmodels.ComposeNotificationViewModel
import com.aroman.mimwallet.presentation_compose.ui.viewmodels.ComposeThemeViewModel
import com.aroman.mimwallet.presentation_compose.ui.viewmodels.ComposeWalletViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComposeActivity : AppCompatActivity() {
    private val themeViewModel by viewModels<ComposeThemeViewModel>()
    private val walletViewModel by viewModels<ComposeWalletViewModel>()
    private val notificationViewModel by viewModels<ComposeNotificationViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isDarkTheme =
                themeViewModel.isDarkTheme.collectAsState(initial = isSystemInDarkTheme())
            AppTheme(useDarkTheme = isDarkTheme.value) {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = Screen.Portfolio.route) {
                    composable(
                        route = Screen.Portfolio.route
                    ) {
                        PortfolioScreen(navController, themeViewModel, walletViewModel)
                    }
                    composable(
                        route = Screen.CoinDetails.route,
                    ) {
                        CoinInsertScreen(navController)
                    }
                    composable(
                        route = Screen.PortfolioNotifications.route
                    ) {
                        PortfolioNotificationsScreen(notificationViewModel)
                    }
                }
            }
        }
    }
}