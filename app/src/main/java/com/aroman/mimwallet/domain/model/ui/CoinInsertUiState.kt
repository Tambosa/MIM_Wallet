package com.aroman.mimwallet.domain.model.ui

import androidx.compose.runtime.MutableState
import com.aroman.mimwallet.domain.model.DisplayableCoin

data class CoinInsertUiState(
    val coinList: List<DisplayableCoin>,
    val selectedCoins: List<DisplayableCoin>,
    val searchQuery: MutableState<String>,
    val isLoading: Boolean,
    val isFiltering: Boolean
)