package com.aroman.mimwallet.presentation.ui.coin_insert.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.aroman.mimwallet.presentation.ui.coin_insert.CoinInsertContent
import com.aroman.mimwallet.presentation.ui.viewmodels.CoinInsertViewModel

@Composable
fun CoinInsertScreen(
    navController: NavController,
    coinInsertViewModel: CoinInsertViewModel = hiltViewModel()
) {
    LaunchedEffect(true) {
        coinInsertViewModel.resetSelectedCoins()
        coinInsertViewModel.getCoinList()
    }
    CoinInsertContent(navController = navController, coinInsertViewModel = coinInsertViewModel)
}