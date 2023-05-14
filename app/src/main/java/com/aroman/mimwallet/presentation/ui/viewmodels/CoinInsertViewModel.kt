package com.aroman.mimwallet.presentation.ui.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aroman.mimwallet.domain.model.DisplayableCoin
import com.aroman.mimwallet.domain.model.ui.CoinInsertUiEvent
import com.aroman.mimwallet.domain.model.ui.CoinInsertUiState
import com.aroman.mimwallet.domain.repository.PortfolioRepository
import com.aroman.mimwallet.domain.use_case.get_coins.GetCoinsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinInsertViewModel @Inject constructor(
    private val getCoinsUseCase: GetCoinsUseCase,
    private val localRepo: PortfolioRepository
) : ViewModel() {
    val state = MutableStateFlow(
        CoinInsertUiState(
            coinList = listOf(),
            selectedCoins = listOf(),
            searchQuery = mutableStateOf(""),
            isLoading = false,
            isFiltering = false
        )
    )

    fun onEvent(event: CoinInsertUiEvent) {
        when (event) {
            is CoinInsertUiEvent.ShowData -> {
                getCoinList()
            }

            is CoinInsertUiEvent.Search -> {
                onSearchQuery(event.query)
            }

            is CoinInsertUiEvent.UpdateSelectedCoins -> {
                updateSelectedCoins(event.coin)
            }

            is CoinInsertUiEvent.ResetSelectedCoins -> {
                resetSelectedCoins()
            }

            is CoinInsertUiEvent.InsertCoin -> {
                insertCoin(event.coin)
            }
        }
    }

    private fun getCoinList() {
        getCoinsUseCase().onEach { result ->
            state.value = result
        }.launchIn(viewModelScope)
    }

    private fun onSearchQuery(newSearchQuery: String) {
        state.value.searchQuery.value = newSearchQuery
    }

    private fun updateSelectedCoins(coin: DisplayableCoin) {
        if (state.value.selectedCoins.contains(coin)) {
            state.value = state.value.copy(selectedCoins = state.value.selectedCoins.minus(coin))
        } else {
            state.value = state.value.copy(selectedCoins = state.value.selectedCoins.plus(coin))
        }
    }

    private fun resetSelectedCoins() {
        state.value = state.value.copy(selectedCoins = listOf())
    }

    private fun insertCoin(coin: DisplayableCoin) {
        viewModelScope.launch {
            localRepo.saveCoin(coin)
        }
    }
}