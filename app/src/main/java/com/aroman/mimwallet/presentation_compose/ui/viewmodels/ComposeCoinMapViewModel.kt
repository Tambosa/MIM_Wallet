package com.aroman.mimwallet.presentation_compose.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aroman.mimwallet.common.ViewState
import com.aroman.mimwallet.domain.model.DisplayableCoin
import com.aroman.mimwallet.domain.repository.PortfolioRepository
import com.aroman.mimwallet.domain.use_case.get_coins.GetCoinsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class ComposeCoinMapViewModel @Inject constructor(
    private val getCoinsUseCase: GetCoinsUseCase,
    private val localRepo: PortfolioRepository
) : ViewModel() {
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _coins = MutableStateFlow<ViewState<List<DisplayableCoin>>>(ViewState.Loading())
    val isLoading = _coins.map { it is ViewState.Loading }
    private val successCoins =
        _coins.map { if (it is ViewState.Success) it.successData else listOf() }

    val coins = searchText
        .debounce(500L)
        .onEach { _isSearching.update { true } }
        .combine(successCoins) { text, coins ->
            if (text.isBlank()) {
                Log.d("@@@", "coins blank: $coins")
                coins
            } else {
                Log.d(
                    "@@@", "coins filter: $coins"
                )
                delay(500)
                coins.filter { it.doesMatchSearchQuery(text) }
            }
        }
        .onEach { _isSearching.update { false } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _coins.value.data
        )

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun getCoins() {
        getCoinsUseCase().onEach { result ->
            _coins.value = result
        }.launchIn(viewModelScope)
    }

    fun insertCoin(coin: DisplayableCoin) {
        viewModelScope.launch {
            localRepo.saveCoin(coin)
        }
    }
}