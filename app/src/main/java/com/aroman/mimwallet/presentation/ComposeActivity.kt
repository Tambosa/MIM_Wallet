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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aroman.mimwallet.presentation.ui.coin_insert.components.CoinInsertScreen
import com.aroman.mimwallet.presentation.ui.portfolio.PortfolioScreen
import com.aroman.mimwallet.presentation.ui.portfolio_notifications.PortfolioNotificationsScreen
import com.aroman.mimwallet.presentation.ui.theme.AppTheme
import com.aroman.mimwallet.presentation.ui.viewmodels.ThemeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ComposeActivity : AppCompatActivity() {
    private val themeViewModel by viewModels<ThemeViewModel>()

    private var keepSplash = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val splashScreen = installSplashScreen()
            splashScreen.setKeepOnScreenCondition { keepSplash }
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.CREATED) {
                    delay(1000)
                    keepSplash = false
                }
            }
        }
        setContent {
            val isDarkTheme = themeViewModel.isDarkTheme.collectAsState(isSystemInDarkTheme())
            val inverseTheme = remember { themeViewModel::inverseTheme }
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
                        composable(route = Screen.Portfolio.route) {
                            PortfolioScreen(navController, inverseTheme)
                        }
                        composable(route = Screen.CoinInsert.route) {
                            CoinInsertScreen(navController)
                        }
                        composable(route = Screen.PortfolioNotifications.route) {
                            PortfolioNotificationsScreen()
                        }
                    }
                }
            }
        }
    }
}