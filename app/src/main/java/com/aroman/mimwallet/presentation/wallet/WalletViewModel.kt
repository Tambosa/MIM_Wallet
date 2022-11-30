package com.aroman.mimwallet.presentation.wallet

import android.util.Log
import androidx.lifecycle.LiveData
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

    private val _coins = MutableLiveData<List<DisplayableCoin>>()
    val coins: LiveData<List<DisplayableCoin>> = _coins

    private val _portfolio = MutableLiveData<List<DisplayableCoin>>()
    val portfolio = _portfolio

    fun getCoins() {
        getCoinsUseCase().onEach { result ->
            when (result) {
                is ViewState.Success -> {
                    _coins.postValue(result.data ?: emptyList())
                }
                is ViewState.Error -> {
                    result.message?.let { Log.d("@@@", it) }
                }
                is ViewState.Loading -> {
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getLocalCoins() {
        viewModelScope.launch {
        }
    }

    fun insertCoin(coin: DisplayableCoin) {
        viewModelScope.launch { localRepo.saveCoin(coin) }
    }

    fun deleteCoin(coin: DisplayableCoin) {
        viewModelScope.launch { localRepo.deleteCoin(coin) }
    }

    fun updateCoin(coin: DisplayableCoin) {
        viewModelScope.launch { localRepo.updateCoin(coin) }
    }
}