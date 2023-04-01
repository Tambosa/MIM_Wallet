package com.aroman.mimwallet.presentation.ui.portfolio.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aroman.mimwallet.domain.model.DisplayableCoin
import com.aroman.mimwallet.domain.model.PortfolioState
import com.aroman.mimwallet.presentation.ui.viewmodels.WalletViewModel
import com.aroman.mimwallet.presentation.ui.viewmodels.WalletViewModel.TimePeriod

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun CoinContent(
    portfolio: PortfolioState,
    timePeriodSelection: TimePeriod,
    navController: NavController,
    viewModel: WalletViewModel,
) {
    var openDialog by remember { mutableStateOf(false) }
    var oldCount by remember { mutableStateOf(0.0) }
    var clickedCoin by remember { mutableStateOf(DisplayableCoin(1, "Bitcoin", "BTC")) }

    LazyColumn(
        contentPadding = PaddingValues(vertical = 15.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (portfolio.coinList.isEmpty()) {
            items(1,
                itemContent = {
                    DisplayableHint()
                    DisplayableInsertCoinButton(navController)
                })
        }
        if (portfolio.coinList.isNotEmpty()) {
            items(
                count = portfolio.coinList.size,
                key = { index -> portfolio.coinList[index].id },
                itemContent = { index ->
                    if (index == 0) {
                        if (portfolio.totalPrice != 0.0) {
                            key(portfolio.coinList) {
                                PieChart(coins = portfolio.coinList)
                            }
                            TotalPrice(
                                portfolio = portfolio,
                                timePeriodSelection = timePeriodSelection
                            )
                        }
                        TimePeriodSelection(
                            walletViewModel = viewModel,
                            timePeriodSelection = timePeriodSelection
                        )
                    }
                    val currentItem by rememberUpdatedState(portfolio.coinList[index])
                    val dismissState = rememberDismissState(
                        confirmStateChange = {
                            when (it) {
                                DismissValue.DismissedToStart -> {
                                    viewModel.deleteCoin(currentItem)
                                    true
                                }
                                else -> false
                            }
                        }
                    )
                    SwipeToDismiss(
                        modifier = Modifier
                            .padding(vertical = 1.dp)
                            .animateItemPlacement(),
                        state = dismissState,
                        background = {
                            SwipeBackground(dismissState)
                        },
                        directions = setOf(DismissDirection.EndToStart),
                        dismissContent = {
                            DisplayableCoinItem(
                                modifier = Modifier
                                    .clickable {
                                        openDialog = true
                                        clickedCoin = portfolio.coinList[index]
                                        oldCount = clickedCoin.count
                                    }
                                    .background(
                                        shape = MaterialTheme.shapes.small,
                                        color = MaterialTheme.colorScheme.primaryContainer
                                    )
                                    .padding(start = 12.dp, end = 12.dp),
                                coin = portfolio.coinList[index],
                                timePeriodSelection = timePeriodSelection,
                            )
                        }
                    )
                    if (index == portfolio.coinList.size - 1) {
                        DisplayableInsertCoinButton(navController)
                    }
                }
            )
        }
    }
    if (openDialog) {
        EditCoinCountDialog(
            onDismissRequest = { openDialog = false },
            clickedCoin = clickedCoin,
            oldCount = oldCount,
            viewModel = viewModel
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun SwipeBackground(dismissState: DismissState) {
    val direction = dismissState.dismissDirection ?: return
    val brush =
        Brush.horizontalGradient(listOf(Color.Red, MaterialTheme.colorScheme.primaryContainer))
    val alignment = when (direction) {
        DismissDirection.StartToEnd -> Alignment.CenterStart
        DismissDirection.EndToStart -> Alignment.CenterEnd
    }
    val icon = when (direction) {
        DismissDirection.StartToEnd -> Icons.Default.Done
        DismissDirection.EndToStart -> Icons.Default.Delete
    }
    val scale by animateFloatAsState(
        if (dismissState.targetValue == DismissValue.Default) 0.75f else 1.25f
    )
    Box(
        Modifier
            .fillMaxSize()
            .background(brush)
            .padding(horizontal = 20.dp),
        contentAlignment = alignment
    ) {
        Icon(
            icon,
            contentDescription = "Localized description",
            modifier = Modifier.scale(scale)
        )
    }
}

@Composable
private fun EditCoinCountDialog(
    onDismissRequest: () -> Unit,
    clickedCoin: DisplayableCoin,
    oldCount: Double,
    viewModel: WalletViewModel
) {
    var saveEnabled by remember { mutableStateOf(true) }
    var newCount by remember { mutableStateOf(oldCount) }
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = { Text(text = clickedCoin.name) },
        text = {
            Column {
                OutlinedTextField(
                    label = { Text(text = "Quantity") },
                    value = newCount.toString(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    onValueChange = {
                        newCount = it.toDoubleOrNull() ?: 0.0
                        saveEnabled = newCount >= 0.0
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = ImeAction.Done
                    )
                )
            }
        },
        confirmButton = {
            Button(
                modifier = Modifier.padding(8.dp),
                onClick = {
                    clickedCoin.count = newCount
                    viewModel.insertCoin(clickedCoin)
                    onDismissRequest()
                },
                enabled = saveEnabled
            ) {
                Text(text = "Confirm")
            }
        },
        dismissButton = {
            Button(
                modifier = Modifier.padding(8.dp),
                onClick = {
                    viewModel.deleteCoin(clickedCoin)
                    viewModel.getPortfolio()
                    onDismissRequest()
                },
            ) {
                Text(text = "Delete")
            }
        }
    )
}