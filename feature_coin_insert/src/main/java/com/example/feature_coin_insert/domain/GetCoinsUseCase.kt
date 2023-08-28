package com.example.feature_coin_insert.domain

import androidx.compose.runtime.mutableStateOf
import com.example.data_network.domain.repository.CoinRepository
import com.example.feature_coin_insert.domain.ui.CoinInsertUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCoinsUseCase @Inject constructor(private val repo: CoinRepository) {
    operator fun invoke(): Flow<CoinInsertUiState> = flow {
        try {
            emit(
                CoinInsertUiState(
                    coinList = listOf(),
                    selectedCoins = mutableListOf(),
                    searchQuery = mutableStateOf(""),
                    isLoading = true,
                    isFiltering = false
                )
            )
            emit(
                CoinInsertUiState(
                    coinList = repo.getCoins(),
                    selectedCoins = mutableListOf(),
                    searchQuery = mutableStateOf(""),
                    isLoading = false,
                    isFiltering = false
                )
            )
        } catch (e: Exception) {
            //nothing
        }
    }
}