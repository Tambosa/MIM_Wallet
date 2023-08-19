package com.aroman.mimwallet.domain.repository

import com.aroman.mimwallet.domain.model.ui.PortfolioUiState

interface CacheUiStateRepo {

    suspend fun savePortfolioState(portfolio: PortfolioUiState)

    suspend fun getPortfolioState(): PortfolioUiState?
}