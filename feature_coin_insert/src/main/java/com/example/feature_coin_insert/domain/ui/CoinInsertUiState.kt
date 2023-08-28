package com.example.feature_coin_insert.domain.ui

import androidx.compose.runtime.MutableState
import com.example.data_network.domain.entity.DisplayableCoin

data class CoinInsertUiState(
    val coinList: List<DisplayableCoin>,
    val selectedCoins: List<DisplayableCoin>,
    val searchQuery: MutableState<String>,
    val isLoading: Boolean,
    val isFiltering: Boolean
)