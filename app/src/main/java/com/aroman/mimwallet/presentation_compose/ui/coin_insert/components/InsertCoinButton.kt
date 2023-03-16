package com.aroman.mimwallet.presentation_compose.ui.coin_insert.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aroman.mimwallet.domain.model.DisplayableCoin
import com.aroman.mimwallet.presentation_compose.ui.viewmodels.ComposeCoinMapViewModel

@Composable
fun InsertCoinButton(
    selectedCoins: List<DisplayableCoin>,
    coinMapViewModel: ComposeCoinMapViewModel,
    navController: NavController
) {
    if (selectedCoins.isNotEmpty()) {
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                selectedCoins.forEach {
                    coinMapViewModel.insertCoin(it)
                }
                navController.popBackStack()
            },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 40.dp)
        ) {
            Text(text = "Add ${selectedCoins.map { it.name + " " }}")
        }
    } else {
        Spacer(modifier = Modifier.height(48.dp))
    }
}