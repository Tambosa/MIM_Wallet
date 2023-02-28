package com.aroman.mimwallet.presentation_compose.ui.coin_details_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.TextField
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aroman.mimwallet.domain.model.DisplayableCoin
import com.aroman.mimwallet.presentation_compose.ui.viewmodels.ComposeCoinMapViewModel


@Composable
fun CoinInsertScreen(
    navController: NavController,
    coinMapViewModel: ComposeCoinMapViewModel
) {
    val coins by coinMapViewModel.coins.collectAsState()
    LaunchedEffect(true) {
        coinMapViewModel.resetSelectedCoins()
        coinMapViewModel.getCoins()
    }

    val searchText by coinMapViewModel.searchText.collectAsState()
    val isLoading by coinMapViewModel.isLoading.collectAsState(true)
    val isSearching by coinMapViewModel.isSearching.collectAsState()
    val selectedCoins by coinMapViewModel.selectedCoins.collectAsState()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        if (isLoading) {
            LoadingBox()
        } else {
            CoinInsertContent(
                searchText,
                coinMapViewModel,
                navController,
                isSearching,
                coins,
                selectedCoins
            )
        }
    }
}

@Composable
private fun CoinInsertContent(
    searchText: String,
    coinMapViewModel: ComposeCoinMapViewModel,
    navController: NavController,
    isSearching: Boolean,
    coins: List<DisplayableCoin>?,
    selectedCoins: List<DisplayableCoin>
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
        InsertCoinButton(
            selectedCoins = selectedCoins,
            coinMapViewModel = coinMapViewModel,
            navController = navController
        )
        if (isSearching) {
            LoadingBox()
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                items(coins!!) { coin ->
                    SelectableCoinItem(coin, coinMapViewModel)
                }
            }
        }
    }
}

@Composable
private fun SelectableCoinItem(
    coin: DisplayableCoin,
    coinMapViewModel: ComposeCoinMapViewModel
) {
    Text(
        text = "${coin.name}: ${coin.symbol}",
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable {
                coinMapViewModel.updateSelectedCoins(coin)
            }
    )
}

@Composable
private fun LoadingBox() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

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
