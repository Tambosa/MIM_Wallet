package com.example.feature_coin_insert.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.feature_coin_insert.domain.ui.CoinInsertUiEvent
import com.example.feature_coin_insert.presentation.CoinInsertContent
import com.example.feature_coin_insert.presentation.CoinInsertViewModel

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