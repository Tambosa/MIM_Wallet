package com.aroman.mimwallet.domain.model.ui

import com.aroman.mimwallet.domain.model.DisplayableCoin

sealed class CoinInsertUiEvent {
    object ShowData : CoinInsertUiEvent()
    data class Search(val query: String) : CoinInsertUiEvent()
    data class UpdateSelectedCoins(val coin: DisplayableCoin) : CoinInsertUiEvent()
    object ResetSelectedCoins : CoinInsertUiEvent()
    data class InsertCoin(val coin: DisplayableCoin) : CoinInsertUiEvent()
}