package com.aroman.mimwallet.domain.repository

import com.aroman.mimwallet.domain.model.PortfolioState

interface CacheUiStateRepo {

    suspend fun savePortfolioState(portfolio: PortfolioState)

    suspend fun getPortfolioState(): PortfolioState?
}