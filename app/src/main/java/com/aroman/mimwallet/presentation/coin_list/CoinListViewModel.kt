package com.aroman.mimwallet.presentation.coin_list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aroman.mimwallet.common.ViewState
import com.aroman.mimwallet.domain.model.Coin
import com.aroman.mimwallet.domain.use_case.get_coins.GetCoinsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CoinListViewModel @Inject constructor(private val getCoinsUseCase: GetCoinsUseCase) :
    ViewModel() {

    private val _coins = MutableLiveData<List<Coin>>()
    val coins: LiveData<List<Coin>> = _coins

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
}