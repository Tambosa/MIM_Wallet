package com.aroman.mimwallet.presentation.ui.coin_insert

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.aroman.mimwallet.R
import com.aroman.mimwallet.presentation.ui.coin_insert.components.InsertCoinButton
import com.aroman.mimwallet.presentation.ui.coin_insert.components.SelectableCoinItem
import com.aroman.mimwallet.presentation.ui.shared_compose_components.LoadingBox
import com.aroman.mimwallet.presentation.ui.viewmodels.CoinMapViewModel


@Composable
fun CoinInsertScreen(
    navController: NavController,
    coinMapViewModel: CoinMapViewModel = hiltViewModel()
) {
    LaunchedEffect(true) {
        coinMapViewModel.resetSelectedCoins()
        coinMapViewModel.getCoins()
    }

    val coins by coinMapViewModel.coins.collectAsState()
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
                        Text(text = stringResource(R.string.search))
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    ),
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
    }
}