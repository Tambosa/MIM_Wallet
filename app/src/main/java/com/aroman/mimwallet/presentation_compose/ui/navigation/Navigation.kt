package com.aroman.mimwallet.presentation_compose.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.aroman.mimwallet.presentation_compose.ui.coin_details_screen.CoinInsertScreen
import com.aroman.mimwallet.presentation_compose.ui.portfolio_screen.PortfolioScreen
import com.aroman.mimwallet.presentation_compose.ui.viewmodels.ComposeCoinMapViewModel
import com.aroman.mimwallet.presentation_compose.ui.viewmodels.ComposeThemeViewModel
import com.aroman.mimwallet.presentation_compose.ui.viewmodels.ComposeWalletViewModel

@Composable
fun Navigation(
    walletViewModel: ComposeWalletViewModel,
    themeViewModel: ComposeThemeViewModel,
    coinMapViewModel: ComposeCoinMapViewModel
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Portfolio.route) {
        composable(Screen.Portfolio.route) {
            PortfolioScreen(
                navController,
                walletViewModel,
                themeViewModel,
            )
        }
        composable(
            route = Screen.CoinDetails.route,
        ) {
            CoinInsertScreen(
                navController = navController,
                coinMapViewModel = coinMapViewModel,
            )
        }
    }
    navController.navigate(Screen.Portfolio.route)
}