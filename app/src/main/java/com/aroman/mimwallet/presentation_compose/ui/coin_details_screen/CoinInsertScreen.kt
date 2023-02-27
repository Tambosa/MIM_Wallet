package com.aroman.mimwallet.presentation_compose.ui.coin_details_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.TextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aroman.mimwallet.presentation_compose.ui.viewmodels.ComposeCoinMapViewModel


@Composable
fun CoinInsertScreen(
    navController: NavController,
    coinMapViewModel: ComposeCoinMapViewModel
) {
    val coins by coinMapViewModel.coins.collectAsState()
    LaunchedEffect(true) { coinMapViewModel.getCoins() }

    val searchText by coinMapViewModel.searchText.collectAsState()
    val isLoading by coinMapViewModel.isLoading.collectAsState(true)
    val isSearching by coinMapViewModel.isSearching.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TextField(
                value = searchText,
                onValueChange = coinMapViewModel::onSearchTextChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(text = "Search")
                },
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(16.dp))
            if (!coins.isNullOrEmpty())
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                ) {
                    items(coins!!) { coin ->
                        Text(
                            text = "${coin.name}: ${coin.symbol}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp)
                        )
                    }
                }
        }
    }
}