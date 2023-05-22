package com.aroman.mimwallet.presentation.ui.coin_insert.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.aroman.mimwallet.domain.model.DisplayableCoin
import com.aroman.mimwallet.domain.model.ui.CoinInsertUiEvent

@Composable
fun SelectableCoinItem(
    coin: DisplayableCoin,
    onEvent: (CoinInsertUiEvent) -> Unit
) {
    Text(
        text = "${coin.name}: ${coin.symbol}",
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable {
                onEvent(CoinInsertUiEvent.UpdateSelectedCoins(coin))
            }
    )
}