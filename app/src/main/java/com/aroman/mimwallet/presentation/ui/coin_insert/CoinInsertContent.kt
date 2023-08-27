package com.aroman.mimwallet.presentation.ui.coin_insert

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.navigation.NavController
import com.aroman.mimwallet.R
import com.aroman.mimwallet.domain.model.ui.CoinInsertUiEvent
import com.aroman.mimwallet.domain.model.ui.CoinInsertUiState
import com.aroman.mimwallet.presentation.ui.coin_insert.components.InsertCoinButton
import com.aroman.mimwallet.presentation.ui.coin_insert.components.SelectableCoinItem
import com.aroman.mimwallet.presentation.ui.shared_compose_components.LoadingBox
import com.example.core_ui.theme.spacing


@Composable
fun CoinInsertContent(
    navController: NavController,
    state: CoinInsertUiState,
    onEvent: (CoinInsertUiEvent) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        if (state.isLoading) {
            LoadingBox()
        } else {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(
                        horizontal = MaterialTheme.spacing.paddingSmall,
                        vertical = MaterialTheme.spacing.paddingSmall
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.spacerSmall))
                TextField(
                    value = state.searchQuery.value,
                    onValueChange = { onEvent(CoinInsertUiEvent.Search(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(text = stringResource(R.string.search))
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                )
                InsertCoinButton(
                    selectedCoins = state.selectedCoins,
                    onEvent = onEvent,
                    navController = navController
                )
                if (state.isFiltering) {
                    LoadingBox()
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                    ) {
                        val filteredCoins = state.coinList.filter {
                            it.doesMatchSearchQuery(state.searchQuery.value)
                        }
                        items(filteredCoins) { coin ->
                            SelectableCoinItem(coin, onEvent)
                        }
                    }
                }
            }
        }
    }
}