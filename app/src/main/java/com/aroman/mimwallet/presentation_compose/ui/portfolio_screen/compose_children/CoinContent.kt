package com.aroman.mimwallet.presentation_compose.ui.portfolio_screen.compose_children

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.aroman.mimwallet.R
import com.aroman.mimwallet.common.ViewState
import com.aroman.mimwallet.domain.model.DisplayableCoin
import com.aroman.mimwallet.domain.model.Portfolio
import com.aroman.mimwallet.presentation_compose.ui.navigation.Screen
import com.aroman.mimwallet.presentation_compose.ui.theme.Typography
import com.aroman.mimwallet.presentation_compose.ui.viewmodels.ComposeWalletViewModel.TimePeriod
import java.math.RoundingMode
import java.text.DecimalFormat

@Composable
fun CoinContent(
    portfolioState: ViewState<Portfolio>,
    timePeriodSelection: TimePeriod,
    navController: NavController
) {
    var coinList by remember { mutableStateOf(listOf<DisplayableCoin>()) }
    if (portfolioState is ViewState.Success) {
        coinList = portfolioState.successData.coinList
    }
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 15.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (coinList.isEmpty()) {
            items(1,
                itemContent = {
                    DisplayableHint()
                    DisplayableInsertCoin(navController)
                })
        }
        if (coinList.isNotEmpty()) {
            items(
                coinList.size,
                itemContent = {
                    DisplayableCoinItem(
                        coin = coinList[it],
                        timePeriodSelection = timePeriodSelection,
                        navController = navController
                    )
                    if (it == coinList.size - 1) {
                        DisplayableInsertCoin(navController)
                    }
                }
            )
        }
    }
}

@Composable
fun DisplayableCoinItem(
    coin: DisplayableCoin,
    timePeriodSelection: TimePeriod,
    navController: NavController
) {
    Column(modifier = Modifier.clickable {
        navController.navigate(Screen.CoinDetails.withArg("${coin.name}: ${coin.symbol}"))
    }) {
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
        modifier = Modifier.fillMaxWidth(),
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
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        IconButton(
            onClick = {
                navController.navigate(Screen.CoinDetails.withArg("BTC"))
            }) {
            Icon(
                modifier = Modifier.size(100.dp),
                painter = painterResource(id = R.drawable.ic_baseline_add_circle_outline_24),
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = "add coin"
            )
        }
    }
}