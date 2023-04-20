package com.aroman.mimwallet.presentation.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aroman.mimwallet.R
import com.aroman.mimwallet.domain.model.DisplayableCoin
import com.aroman.mimwallet.domain.model.PortfolioUiState
import com.aroman.mimwallet.domain.repository.PortfolioRepository
import com.aroman.mimwallet.domain.use_case.get_portfolio.GetPortfolioUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val getPortfolioUseCase: GetPortfolioUseCase,
    private val localRepo: PortfolioRepository
) : ViewModel() {

    private val _portfolio = MutableStateFlow(PortfolioUiState(listOf(), isCache = false))
    val portfolio = _portfolio.asStateFlow()

    fun getPortfolio() {
        _portfolio.value = _portfolio.value.copy(isLoading = true)
        getPortfolioUseCase().onEach { result ->
            _portfolio.value = result.copy(isLoading = false)
        }.launchIn(viewModelScope)
    }

    fun updateCoin(coin: DisplayableCoin) {
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

    fun setTimePeriod(timePeriod: TimePeriod) {
        viewModelScope.launch {
            _portfolio.value = _portfolio.value.copy(timePeriod = timePeriod)
        }
    }

    enum class TimePeriod(val value: Int) {
        ONE_HOUR(R.string._1h),
        TWENTY_FOUR_HOURS(R.string._24h),
        SEVEN_DAYS(R.string._7d),
        THIRTY_DAYS(R.string._30d),
        SIXTY_DAYS(R.string._60d),
        NINETY_DAYS(R.string._90d),
    }
}