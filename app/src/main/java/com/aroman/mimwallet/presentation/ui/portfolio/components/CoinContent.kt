package com.aroman.mimwallet.presentation.ui.portfolio.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aroman.mimwallet.R
import com.aroman.mimwallet.domain.model.DisplayableCoin
import com.aroman.mimwallet.domain.model.PortfolioUiState
import com.aroman.mimwallet.presentation.ui.shared_compose_components.RoundedButton
import com.aroman.mimwallet.presentation.ui.viewmodels.WalletViewModel

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun CoinContent(
    portfolio: PortfolioUiState,
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
                                timePeriodSelection = portfolio.timePeriod
                            )
                        }
                        TimePeriodSelection(
                            walletViewModel = viewModel,
                            timePeriodSelection = portfolio.timePeriod
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
                                timePeriodSelection = portfolio.timePeriod,
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
            contentDescription = stringResource(id = R.string.delete),
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
    var newCount by remember { mutableStateOf(oldCount.toString()) }
    saveEnabled = newCount.toDouble() != 0.0
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = { Text(text = clickedCoin.name) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    style = MaterialTheme.typography.titleLarge,
                    text = newCount
                )
                val buttonModifier =
                    Modifier
                        .aspectRatio(1f)
                        .weight(1f)
                val buttonSpacing = 15.dp
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
                ) {
                    RoundedButton(symbol = "1", modifier = buttonModifier) {
                        newCount = if (newCount == "0") "1" else newCount + "1"
                    }
                    RoundedButton(symbol = "2", modifier = buttonModifier) {
                        newCount = if (newCount == "0") "2" else newCount + "2"
                    }
                    RoundedButton(symbol = "3", modifier = buttonModifier) {
                        newCount = if (newCount == "0") "3" else newCount + "3"
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
                ) {
                    RoundedButton(symbol = "4", modifier = buttonModifier) {
                        newCount = if (newCount == "0") "4" else newCount + "4"
                    }
                    RoundedButton(symbol = "5", modifier = buttonModifier) {
                        newCount = if (newCount == "0") "5" else newCount + "5"
                    }
                    RoundedButton(symbol = "6", modifier = buttonModifier) {
                        newCount = if (newCount == "0") "6" else newCount + "6"
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
                ) {
                    RoundedButton(symbol = "7", modifier = buttonModifier) {
                        newCount = if (newCount == "0") "7" else newCount + "7"
                    }
                    RoundedButton(symbol = "8", modifier = buttonModifier) {
                        newCount = if (newCount == "0") "8" else newCount + "8"
                    }
                    RoundedButton(symbol = "9", modifier = buttonModifier) {
                        newCount = if (newCount == "0") "9" else newCount + "9"
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    horizontalArrangement = Arrangement.spacedBy(buttonSpacing)
                ) {
                    RoundedButton(symbol = ".", modifier = buttonModifier) {
                        if (!newCount.contains(".")) newCount += "."
                    }
                    RoundedButton(symbol = "0", modifier = buttonModifier) {
                        if (newCount != "0") newCount += "0"
                    }
                    RoundedButton(
                        symbol = "C",
                        modifier = buttonModifier,
                        color = (MaterialTheme.colorScheme.tertiaryContainer),
                        textColor = MaterialTheme.colorScheme.onTertiaryContainer
                    ) {
                        newCount = if (newCount.length == 1) "0" else newCount.dropLast(1)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                modifier = Modifier.padding(8.dp),
                onClick = {
                    clickedCoin.count = newCount.toDouble()
                    viewModel.updateCoin(clickedCoin)
                    onDismissRequest()
                },
                enabled = saveEnabled
            ) {
                Text(text = stringResource(id = R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(
                modifier = Modifier.padding(8.dp),
                onClick = {
                    viewModel.deleteCoin(clickedCoin)
                    viewModel.getPortfolio()
                    onDismissRequest()
                },
            ) {
                Text(
                    text = stringResource(id = R.string.delete),
                    color = Color.Red
                )
            }
        }
    )
}