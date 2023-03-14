package com.aroman.mimwallet.presentation_compose.ui.portfolio_screen.compose_children

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aroman.mimwallet.R
import com.aroman.mimwallet.domain.model.DisplayableCoin
import com.aroman.mimwallet.domain.model.Portfolio
import com.aroman.mimwallet.presentation_compose.Screen
import com.aroman.mimwallet.presentation_compose.ui.theme.Typography
import com.aroman.mimwallet.presentation_compose.ui.viewmodels.ComposeWalletViewModel
import com.aroman.mimwallet.presentation_compose.ui.viewmodels.ComposeWalletViewModel.TimePeriod
import java.math.RoundingMode
import java.text.DecimalFormat

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun CoinContent(
    portfolio: Portfolio,
    timePeriodSelection: TimePeriod,
    navController: NavController,
    viewModel: ComposeWalletViewModel,
) {
    //alertDialog's vars
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
                    DisplayableInsertCoin(navController)
                })
        }
        if (portfolio.coinList.isNotEmpty()) {
            items(
                count = portfolio.coinList.size,
                key = { index -> portfolio.coinList[index].id },
                itemContent = { index ->
                    if (index == 0) {
                        RecyclerTopContent(portfolio, timePeriodSelection, viewModel)
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
                        DisplayableInsertCoin(navController)
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
fun SwipeBackground(dismissState: DismissState) {
    val direction = dismissState.dismissDirection ?: return
    val color by animateColorAsState(Color.Red)
    val alignment = when (direction) {
        DismissDirection.StartToEnd -> Alignment.CenterStart
        DismissDirection.EndToStart -> Alignment.CenterEnd
    }
    val icon = when (direction) {
        DismissDirection.StartToEnd -> Icons.Default.Done
        DismissDirection.EndToStart -> Icons.Default.Delete
    }
    val scale by animateFloatAsState(
        if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
    )
    Box(
        Modifier
            .fillMaxSize()
            .background(color)
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
fun EditCoinCountDialog(
    onDismissRequest: () -> Unit,
    clickedCoin: DisplayableCoin,
    oldCount: Double,
    viewModel: ComposeWalletViewModel
) {
    var saveEnabled by remember { mutableStateOf(true) }
    var newCount by remember { mutableStateOf(oldCount) }
    AlertDialog(
        onDismissRequest = { onDismissRequest() },
        title = { Text(text = clickedCoin.name) },
        text = {
            Column() {
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

@Composable
private fun RecyclerTopContent(
    portfolio: Portfolio,
    timePeriodSelection: TimePeriod,
    viewModel: ComposeWalletViewModel
) {
    key(portfolio.coinList) {
        PieChart(coins = portfolio.coinList)
    }
    TotalPrice(
        portfolio = portfolio,
        timePeriodSelection = timePeriodSelection
    )
    TimePeriodSelection(
        walletViewModel = viewModel,
        timePeriodSelection = timePeriodSelection
    )
}

@Composable
fun DisplayableCoinItem(
    coin: DisplayableCoin,
    timePeriodSelection: TimePeriod,
    modifier: Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val coinPrice by remember { mutableStateOf(coin.price) }
            val coinPriceAnim by animateFloatAsState(
                targetValue = coinPrice.toFloat(), animationSpec = tween(
                    durationMillis = 1500,
                    easing = FastOutSlowInEasing
                )
            )
            Text(
                text = coin.name,
                style = Typography.bodyMedium,
            )
            Text(
                text = String.format("$%.2f", (coinPriceAnim * coin.count)),
                style = Typography.bodyMedium
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${coin.count} ${coin.symbol}",
                style = Typography.bodySmall
            )
            val percentFormat = DecimalFormat("0.##'%'").apply {
                roundingMode = RoundingMode.CEILING
            }
            val percentChange = when (timePeriodSelection) {
                TimePeriod.ONE_HOUR -> coin.percentChange1h
                TimePeriod.TWENTY_FOUR_HOURS -> coin.percentChange24h
                TimePeriod.SEVEN_DAYS -> coin.percentChange7d
                TimePeriod.THIRTY_DAYS -> coin.percentChange30d
                TimePeriod.SIXTY_DAYS -> coin.percentChange60d
                TimePeriod.NINETY_DAYS -> coin.percentChange90d
            }
            Text(
                text = percentFormat.format(percentChange),
                style = Typography.bodySmall,
                color = if (percentChange > 0) Color(android.graphics.Color.GREEN)
                else Color(android.graphics.Color.RED)
            )
        }
    }
}

@Composable
fun DisplayableHint() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Click button below to add your first coin to your portfolio",
            style = Typography.bodyMedium
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_baseline_arrow_downward_24),
            contentDescription = ""
        )
    }
}

@Composable
fun DisplayableInsertCoin(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(
            onClick = { navController.navigate(Screen.CoinDetails.route) }) {
            Icon(
                modifier = Modifier.size(100.dp),
                painter = painterResource(id = R.drawable.ic_baseline_add_circle_outline_24),
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = "add coin"
            )
        }
    }
}