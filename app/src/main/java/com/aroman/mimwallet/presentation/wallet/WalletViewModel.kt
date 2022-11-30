package com.aroman.mimwallet.presentation.wallet

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aroman.mimwallet.common.ViewState
import com.aroman.mimwallet.domain.model.CoinDetails
import com.aroman.mimwallet.domain.model.DisplayableCoin
import com.aroman.mimwallet.domain.repository.PortfolioRepository
import com.aroman.mimwallet.domain.use_case.get_coin_details.GetCoinDetailsUseCase
import com.aroman.mimwallet.domain.use_case.get_coins.GetCoinsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val getCoinsUseCase: GetCoinsUseCase,
    private val getCoinDetailsUseCase: GetCoinDetailsUseCase,
    private val localRepo: PortfolioRepository,
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
            val coinList = localRepo.getAll()
            val priceList = HashMap<String, CoinDetails>()
            for (coin in coinList) {
                getCoinDetailsUseCase(coin.symbol).onEach { result ->
                    when (result) {
                        is ViewState.Success -> {
                            priceList[coin.symbol] = result.data!!.getValue(coin.symbol)
                        }
                        is ViewState.Error -> {
                            result.message?.let { Log.d("@@@", it) }
                        }
                        is ViewState.Loading -> {
                        }
                    }
                }
            }

            val displayableCoinList = mutableListOf<DisplayableCoin>()
            for (coin in coinList) {
                displayableCoinList.add(
                    DisplayableCoin(
                        id = coin.id,
                        name = coin.name,
                        symbol = coin.symbol,
                        count = coin.count,
                        price = priceList[coin.symbol]!!.price,
                        percentChange1h = priceList[coin.symbol]!!.percentChange1h,
                        percentChange24h = priceList[coin.symbol]!!.percentChange24h,
                        percentChange7d = priceList[coin.symbol]!!.percentChange7d,
                        percentChange30d = priceList[coin.symbol]!!.percentChange30d,
                        percentChange60d = priceList[coin.symbol]!!.percentChange60d,
                        percentChange90d = priceList[coin.symbol]!!.percentChange90d,
                    )
                )
            }
            _portfolio.postValue(displayableCoinList)
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