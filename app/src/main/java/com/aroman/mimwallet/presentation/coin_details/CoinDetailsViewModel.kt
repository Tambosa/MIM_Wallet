package com.aroman.mimwallet.presentation.coin_details

import android.util.Log
import androidx.lifecycle.*
import com.aroman.mimwallet.common.ViewState
import com.aroman.mimwallet.domain.model.CoinDetails
import com.aroman.mimwallet.domain.use_case.get_coin_details.GetCoinDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CoinDetailsViewModel @Inject constructor(
    private val getCoinDetailsUseCase: GetCoinDetailsUseCase,
) : ViewModel() {

    private val _coinDetails = MutableLiveData<HashMap<String, CoinDetails>>()
    val coinDetails: LiveData<HashMap<String, CoinDetails>> = _coinDetails

    fun getCoinDetails(coinSymbol: String) {
        getCoinDetailsUseCase(coinSymbol).onEach { result ->
            when (result) {
                is ViewState.Success -> {
                    result.data.let { _coinDetails.postValue(it) }
                }
                is ViewState.Error -> {
                    result.message?.let { Log.d("@@@", it) }
                }
                is ViewState.Loading -> {
                }
            }
        }.launchIn(viewModelScope)
    }
}