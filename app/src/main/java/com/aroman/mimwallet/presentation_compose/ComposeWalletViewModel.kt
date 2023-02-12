package com.aroman.mimwallet.presentation_compose

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aroman.mimwallet.common.ViewState
import com.aroman.mimwallet.domain.model.DisplayableCoin
import com.aroman.mimwallet.domain.model.Portfolio
import com.aroman.mimwallet.domain.repository.PortfolioRepository
import com.aroman.mimwallet.domain.use_case.get_coins.GetCoinsUseCase
import com.aroman.mimwallet.domain.use_case.get_portfolio.GetPortfolioUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComposeWalletViewModel @Inject constructor(
    private val getCoinsUseCase: GetCoinsUseCase,
    private val getPortfolioUseCase: GetPortfolioUseCase,
    private val localRepo: PortfolioRepository
) :
    ViewModel() {

    private val _coins = MutableStateFlow<ViewState<List<DisplayableCoin>>>(ViewState.Loading())
    val coins = _coins.asStateFlow()

    private val _portfolio = MutableStateFlow<ViewState<Portfolio>>(
        ViewState.Success(Portfolio(listOf()))
    )
    val portfolio = _portfolio.asStateFlow()

    fun getCoins() {
        getCoinsUseCase().onEach { result ->
            _coins.value = result
        }.launchIn(viewModelScope)
    }

    fun getPortfolio() {
        getPortfolioUseCase().onEach { result ->
            _portfolio.value = result
        }.launchIn(viewModelScope)
    }

    fun insertCoin(coin: DisplayableCoin) {
        viewModelScope.launch {
            localRepo.saveCoin(coin)
            getPortfolio()
        }
    }

    fun deleteCoin(coin: DisplayableCoin) {
        viewModelScope.launch {
            localRepo.deleteCoin(coin)
            getPortfolio()
        }
    }
}