package com.aroman.mimwallet.presentation.coin_details

import android.util.Log
import androidx.lifecycle.*
import com.aroman.mimwallet.common.Constants
import com.aroman.mimwallet.common.Resource
import com.aroman.mimwallet.domain.model.CoinDetails
import com.aroman.mimwallet.domain.use_case.get_coin_details.GetCoinDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CoinDetailsViewModel @Inject constructor(
    private val getCoinDetailsUseCase: GetCoinDetailsUseCase,
    savedStateHandle: SavedStateHandle
) :
    ViewModel() {

    private val _coins = MutableLiveData<CoinDetails>()
    val coins: LiveData<CoinDetails> = _coins

    init {
        savedStateHandle.get<String>(Constants.PARAM_COIN_SYMBOL)?.let { coinSymbol ->
            getCoin(coinSymbol)
        }
    }

    private fun getCoin(coinSymbol: String) {
        getCoinDetailsUseCase(coinSymbol).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    result.data.let { _coins.postValue(it) }
                }
                is Resource.Error -> {
                    result.message?.let { Log.d("@@@", it) }
                }
                is Resource.Loading -> {
                }
            }
        }.launchIn(viewModelScope)
    }
}