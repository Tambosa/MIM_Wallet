package com.aroman.mimwallet.presentation_compose.ui.coin_details_screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun CoinDetailsScreen(symbol: String?) {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Text(text = symbol ?: "nothing")
    }
}
