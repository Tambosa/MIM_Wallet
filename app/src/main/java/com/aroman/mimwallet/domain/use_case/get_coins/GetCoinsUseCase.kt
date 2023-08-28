package com.aroman.mimwallet.domain.use_case.get_coins

import androidx.compose.runtime.mutableStateOf
import com.aroman.mimwallet.domain.model.ui.CoinInsertUiState
import com.example.data_network.domain.repository.CoinRepository
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