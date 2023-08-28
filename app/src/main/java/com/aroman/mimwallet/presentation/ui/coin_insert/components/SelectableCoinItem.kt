package com.aroman.mimwallet.presentation.ui.coin_insert.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.aroman.mimwallet.domain.model.ui.CoinInsertUiEvent
import com.example.core_ui.theme.spacing
import com.example.data_network.domain.entity.DisplayableCoin

@Composable
fun SelectableCoinItem(
    coin: DisplayableCoin,
    onEvent: (CoinInsertUiEvent) -> Unit
) {
    Text(
        text = "${coin.name}: ${coin.symbol}",
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = MaterialTheme.spacing.paddingSmall)
            .clickable {
                onEvent(CoinInsertUiEvent.UpdateSelectedCoins(coin))
            }
    )
}