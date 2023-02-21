package com.aroman.mimwallet.presentation_compose.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.aroman.mimwallet.presentation_compose.ui.coin_details_screen.CoinDetailsScreen
import com.aroman.mimwallet.presentation_compose.ui.portfolio_screen.PortfolioScreen
import com.aroman.mimwallet.presentation_compose.ui.viewmodels.ComposeThemeViewModel
import com.aroman.mimwallet.presentation_compose.ui.viewmodels.ComposeWalletViewModel

@Composable
fun Navigation(
    walletViewModel: ComposeWalletViewModel,
    themeViewModel: ComposeThemeViewModel,
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
            route = "${Screen.CoinDetails.route}/{symbol}",
            arguments = listOf(
                navArgument("symbol") {
                    type = NavType.StringType
                    defaultValue = ""
                    nullable = true
                }
            )
        ) { entry ->
            CoinDetailsScreen(symbol = entry.arguments?.getString("symbol"))
        }
    }
    navController.navigate(Screen.Portfolio.route)
}