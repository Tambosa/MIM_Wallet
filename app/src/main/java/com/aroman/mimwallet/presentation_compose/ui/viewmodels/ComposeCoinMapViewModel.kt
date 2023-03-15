package com.aroman.mimwallet.presentation_compose.ui.viewmodels

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

    private val _selectedCoins = MutableStateFlow(listOf<DisplayableCoin>())
    val selectedCoins = _selectedCoins.asStateFlow()

    val coins = searchText
        .debounce(500L)
        .onEach { _isSearching.update { true } }
        .combine(successCoins) { text, coins ->
            if (text.isBlank()) {
                coins
            } else {
                delay(100)
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

    fun resetSelectedCoins() {
        _selectedCoins.value = listOf()
    }

    fun updateSelectedCoins(coin: DisplayableCoin) {
        _selectedCoins.value = if (selectedCoins.value.contains(coin)) {
            mutableListOf<DisplayableCoin>().apply {
                addAll(_selectedCoins.value)
                remove(coin)
            }
        } else {
            listOf(*_selectedCoins.value.toTypedArray(), coin)
        }
    }
}