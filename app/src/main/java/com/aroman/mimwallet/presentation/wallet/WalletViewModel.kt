package com.aroman.mimwallet.presentation.wallet

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aroman.mimwallet.common.ViewState
import com.aroman.mimwallet.domain.model.DisplayableCoin
import com.aroman.mimwallet.domain.repository.PortfolioRepository
import com.aroman.mimwallet.domain.use_case.get_coins.GetCoinsUseCase
import com.aroman.mimwallet.domain.use_case.get_portfolio.GetPortfolioUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val getCoinsUseCase: GetCoinsUseCase,
    private val getPortfolioUseCase: GetPortfolioUseCase,
    private val localRepo: PortfolioRepository
) :
    ViewModel() {

    private val _coins = MutableLiveData<ViewState<List<DisplayableCoin>>>()
    val coins = _coins

    private val _portfolio = MutableLiveData<ViewState<List<DisplayableCoin>>>()
    val portfolio = _portfolio

    fun getCoins() {
        getCoinsUseCase().onEach { result ->
            _coins.postValue(result)
        }.launchIn(viewModelScope)
    }

    fun getPortfolio() {
        getPortfolioUseCase().onEach { result ->
            _portfolio.postValue(result)
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