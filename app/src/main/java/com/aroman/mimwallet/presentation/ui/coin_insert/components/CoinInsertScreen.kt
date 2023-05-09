package com.aroman.mimwallet.presentation.ui.coin_insert.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.aroman.mimwallet.presentation.ui.coin_insert.CoinInsertContent
import com.aroman.mimwallet.presentation.ui.viewmodels.CoinMapViewModel

@Composable
fun CoinInsertScreen(
    navController: NavController,
    coinMapViewModel: CoinMapViewModel = hiltViewModel()
) {
    LaunchedEffect(true) {
        coinMapViewModel.resetSelectedCoins()
        coinMapViewModel.getCoins()
    }
    CoinInsertContent(navController = navController, coinMapViewModel = coinMapViewModel)
}