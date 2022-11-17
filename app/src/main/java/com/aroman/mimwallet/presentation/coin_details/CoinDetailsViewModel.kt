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
) :
    ViewModel() {

    private val _coinDetails = MutableLiveData<CoinDetails>()
    val coinDetails: LiveData<CoinDetails> = _coinDetails

    fun getCoin(coinSymbol: String) {
        getCoinDetailsUseCase(coinSymbol).onEach { result ->
            when (result) {
                is Resource.Success -> {
                    result.data.let { _coinDetails.postValue(it) }
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