package com.aroman.mimwallet.presentation.ui.coin_insert.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.aroman.mimwallet.domain.model.ui.CoinInsertUiEvent
import com.aroman.mimwallet.presentation.ui.coin_insert.CoinInsertContent
import com.aroman.mimwallet.presentation.ui.viewmodels.CoinInsertViewModel

@Composable
fun CoinInsertScreen(
    navController: NavController,
    coinInsertViewModel: CoinInsertViewModel = hiltViewModel()
) {
    val state by coinInsertViewModel.state.collectAsState()

    LaunchedEffect(true) {
        coinInsertViewModel.onEvent(CoinInsertUiEvent.ResetSelectedCoins)
        coinInsertViewModel.onEvent(CoinInsertUiEvent.ShowData)
    }
    CoinInsertContent(
        navController = navController,
        state = state,
        onEvent = coinInsertViewModel::onEvent
    )
}