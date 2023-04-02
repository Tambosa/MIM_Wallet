package com.aroman.mimwallet.presentation.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aroman.mimwallet.R
import com.aroman.mimwallet.common.ViewState
import com.aroman.mimwallet.domain.model.DisplayableCoin
import com.aroman.mimwallet.domain.model.PortfolioState
import com.aroman.mimwallet.domain.repository.PortfolioRepository
import com.aroman.mimwallet.domain.use_case.get_portfolio.GetPortfolioUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletViewModel @Inject constructor(
    private val getPortfolioUseCase: GetPortfolioUseCase,
    private val localRepo: PortfolioRepository
) :
    ViewModel() {

    private val _portfolio = MutableStateFlow<ViewState<PortfolioState>>(ViewState.Loading())
    val portfolio = _portfolio.asStateFlow()
    val isLoading = portfolio.map { it is ViewState.Loading }

    private val _timePeriod = MutableStateFlow(TimePeriod.TWENTY_FOUR_HOURS)
    val timePeriod = _timePeriod.asStateFlow()

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

    fun setTimePeriod(value: TimePeriod) {
        viewModelScope.launch {
            _timePeriod.emit(value)
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