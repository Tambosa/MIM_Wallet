package com.example.feature_coin_insert.presentation.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.core_ui.R
import com.example.core_ui.theme.spacing
import com.example.data_network.domain.entity.DisplayableCoin
import com.example.feature_coin_insert.domain.ui.CoinInsertUiEvent

@Composable
fun InsertCoinButton(
    selectedCoins: List<DisplayableCoin>,
    onEvent: (CoinInsertUiEvent) -> Unit,
    navController: NavController
) {
    if (selectedCoins.isNotEmpty()) {
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                selectedCoins.forEach {
                    it.count = 1.0
                    onEvent(CoinInsertUiEvent.InsertCoin(it))
                }
                navController.popBackStack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 40.dp)
        ) {
            Text(text = "${stringResource(R.string.add)}   ${selectedCoins.map { it.name + " " }}")
        }
    } else {
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.spacerMedium))
    }
}