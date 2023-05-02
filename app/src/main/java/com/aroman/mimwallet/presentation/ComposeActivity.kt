package com.aroman.mimwallet.presentation

import android.os.Build
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aroman.mimwallet.presentation.ui.coin_insert.CoinInsertScreen
import com.aroman.mimwallet.presentation.ui.portfolio.PortfolioScreen
import com.aroman.mimwallet.presentation.ui.portfolio_notifications.PortfolioNotificationsScreen
import com.aroman.mimwallet.presentation.ui.theme.AppTheme
import com.aroman.mimwallet.presentation.ui.viewmodels.CoinMapViewModel
import com.aroman.mimwallet.presentation.ui.viewmodels.NoticePortfolioViewModel
import com.aroman.mimwallet.presentation.ui.viewmodels.PortfolioViewModel
import com.aroman.mimwallet.presentation.ui.viewmodels.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class ComposeActivity : AppCompatActivity() {
    private val themeViewModel by viewModels<ThemeViewModel>()
    private val portfolioViewModel by viewModels<PortfolioViewModel>()
    private val noticePortfolioViewModel by viewModels<NoticePortfolioViewModel>()
    private val coinMapViewModel by viewModels<CoinMapViewModel>()

    private var keepSplash = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val splashScreen = installSplashScreen()
            splashScreen.setKeepOnScreenCondition { keepSplash }
            lifecycleScope.launchWhenCreated {
                delay(1000)
                keepSplash = false
            }
        }
        setContent {
            val isDarkTheme = themeViewModel.isDarkTheme.collectAsState(isSystemInDarkTheme())
            val onThemeChange = remember { themeViewModel::inverseTheme }
            val noticePortfolioState by noticePortfolioViewModel.noticePortfolioState.collectAsState()
            val onNoticePortfolioUiEvent = remember { noticePortfolioViewModel::onEvent }
            val portfolioState by portfolioViewModel.portfolio.collectAsState()
            val onPortfolioUiEvent = remember { portfolioViewModel::onEvent }

            AppTheme(useDarkTheme = isDarkTheme.value) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {

                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Portfolio.route
                    ) {
                        composable(
                            route = Screen.Portfolio.route
                        ) {
                            PortfolioScreen(
                                navController = navController,
                                onThemeChange = onThemeChange,
                                state = portfolioState,
                                onEvent = onPortfolioUiEvent
                            )
                        }
                        composable(
                            route = Screen.CoinDetails.route,
                        ) {
                            CoinInsertScreen(navController, coinMapViewModel)
                        }
                        composable(
                            route = Screen.PortfolioNotifications.route
                        ) {
                            PortfolioNotificationsScreen(
                                noticePortfolioUiState = noticePortfolioState,
                                onEvent = onNoticePortfolioUiEvent
                            )
                        }
                    }
                }
            }
        }
    }
}