package com.aroman.mimwallet.presentation_compose.ui.coin_details_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aroman.mimwallet.presentation_compose.ui.coin_details_screen.compose_children.DialogDropDownMenu
import com.aroman.mimwallet.presentation_compose.ui.navigation.Screen
import com.aroman.mimwallet.presentation_compose.ui.viewmodels.ComposeWalletViewModel

@Composable
fun CoinInsertScreen(
    navController: NavController,
    walletViewModel: ComposeWalletViewModel,
) {
    val coins by walletViewModel.coins.collectAsState()
    val coinsList by remember {
        derivedStateOf {
            coins.data?.map { "${it.name}: ${it.symbol}" } ?: listOf()
        }
    }
    var selectedIndex by remember { mutableStateOf(-1) }
    var saveEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(true) {
        walletViewModel.getCoins()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primaryContainer
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            DialogDropDownMenu(
                modifier = Modifier.padding(vertical = 12.dp),
                label = "Crypto currency",
                items = coinsList,
                onItemSelected = { index, _ ->
                    selectedIndex = index
                },
                selectedIndex = selectedIndex
            )

            var coinCount by remember { mutableStateOf(0.0) }
            OutlinedTextField(
                label = { Text("Quantity") },
                value = coinCount.toString(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                onValueChange = { coinCount = it.toDoubleOrNull() ?: 0.0 },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done
                )
            )

            saveEnabled = selectedIndex != -1 && coinCount > 0.0
            Button(
                onClick = {
                    val newCoin = coins.data!![selectedIndex]
                    newCoin.count = coinCount
                    walletViewModel.insertCoin(newCoin)
                    navController.navigate(Screen.Portfolio.route)
                },
                enabled = saveEnabled
            ) {
                Text(text = "Save")
            }
        }
    }
}