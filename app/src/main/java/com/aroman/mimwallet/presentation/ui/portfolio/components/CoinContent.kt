package com.aroman.mimwallet.presentation.ui.portfolio.components

import android.content.res.Configuration
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aroman.mimwallet.R
import com.aroman.mimwallet.domain.model.DisplayableCoin
import com.aroman.mimwallet.domain.model.ui.PortfolioUiEvent
import com.aroman.mimwallet.domain.model.ui.PortfolioUiState

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun CoinContent(
    state: PortfolioUiState,
    onEvent: (PortfolioUiEvent) -> Unit,
    navController: NavController,
) {
    var openDialog by remember { mutableStateOf(false) }
    var oldCount by remember { mutableStateOf(0.0) }
    var clickedCoin by remember { mutableStateOf(DisplayableCoin(1, "Bitcoin", "BTC")) }
    val contentBlur by animateFloatAsState(targetValue = if (openDialog) 1f else 0f)
    LazyColumn(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .fillMaxHeight()
            .blur(radius = (contentBlur * 16).dp),
        contentPadding = PaddingValues(vertical = 15.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (state.coinList.isEmpty()) {
            items(1,
                itemContent = {
                    DisplayableHint()
                    DisplayableInsertCoinButton(navController)
                })
        }
        if (state.coinList.isNotEmpty()) {
            items(
                count = state.coinList.size,
                key = { index -> state.coinList[index].id },
                itemContent = { index ->
                    if (index == 0) {
                        if (state.totalPrice != 0.0) {
                            key(state.coinList) {
                                PieChart(coins = state.coinList)
                            }
                            TotalPrice(
                                portfolio = state,
                                timePeriodSelection = state.timePeriod
                            )
                        }
                        TimePeriodSelection(
                            onEvent = onEvent,
                            timePeriodSelection = state.timePeriod
                        )
                    }
                    val currentItem by rememberUpdatedState(state.coinList[index])
                    val dismissState = rememberDismissState(
                        confirmStateChange = {
                            when (it) {
                                DismissValue.DismissedToStart -> {
                                    onEvent(PortfolioUiEvent.DeleteCoin(currentItem))
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
                                        clickedCoin = state.coinList[index]
                                        oldCount = clickedCoin.count
                                    }
                                    .background(
                                        shape = MaterialTheme.shapes.small,
                                        color = MaterialTheme.colorScheme.primaryContainer
                                    )
                                    .padding(start = 12.dp, end = 12.dp),
                                coin = state.coinList[index],
                                timePeriodSelection = state.timePeriod,
                            )
                        }
                    )
                    if (index == state.coinList.size - 1) {
                        DisplayableInsertCoinButton(navController)
                    }
                }
            )
        }
    }
    val configuration = LocalConfiguration.current
    if (openDialog) {
        when (configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT ->
                EditCoinCountPortretDialog(
                    onDismissRequest = { openDialog = false },
                    clickedCoin = clickedCoin,
                    oldCount = oldCount,
                    onEvent = onEvent,
                )

            else ->
                EditCoinCountLandscapeDialog(
                    onDismissRequest = { openDialog = false },
                    clickedCoin = clickedCoin,
                    oldCount = oldCount,
                    onEvent = onEvent,
                )
        }

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

