package com.aroman.mimwallet.presentation.ui.portfolio

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.aroman.mimwallet.presentation.ui.viewmodels.PortfolioViewModel

@Composable
fun PortfolioScreen(
    navController: NavController,
    inverseTheme: () -> Unit,
    portfolioViewModel: PortfolioViewModel = hiltViewModel()
) {
    val portfolioState by portfolioViewModel.portfolio.collectAsState()
    val onPortfolioUiEvent = remember { portfolioViewModel::onEvent }
    PortfolioScreenContent(
        navController = navController,
        onThemeChange = inverseTheme,
        state = portfolioState,
        onEvent = onPortfolioUiEvent
    )
}