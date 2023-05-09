package com.aroman.mimwallet.presentation.ui.portfolio

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.aroman.mimwallet.presentation.ui.viewmodels.PortfolioViewModel
import com.aroman.mimwallet.presentation.ui.viewmodels.ThemeViewModel

@Composable
fun PortfolioScreen(
    navController: NavController,
    portfolioViewModel: PortfolioViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel = hiltViewModel()
) {
    val onThemeChange = remember { themeViewModel::inverseTheme }
    val portfolioState by portfolioViewModel.portfolio.collectAsState()
    val onPortfolioUiEvent = remember { portfolioViewModel::onEvent }
    PortfolioScreenContent(
        navController = navController,
        onThemeChange = onThemeChange,
        state = portfolioState,
        onEvent = onPortfolioUiEvent
    )
}