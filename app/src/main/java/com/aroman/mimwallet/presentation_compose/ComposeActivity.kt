package com.aroman.mimwallet.presentation_compose

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aroman.mimwallet.R
import com.aroman.mimwallet.common.ViewState
import com.aroman.mimwallet.domain.model.DisplayableCoin
import com.aroman.mimwallet.domain.model.Portfolio
import com.aroman.mimwallet.presentation_compose.ui.theme.AppTheme
import com.aroman.mimwallet.presentation_compose.ui.theme.Typography
import dagger.hilt.android.AndroidEntryPoint
import java.math.RoundingMode
import java.text.DecimalFormat

@AndroidEntryPoint
class ComposeActivity : AppCompatActivity() {
    private val walletViewModel by viewModels<ComposeWalletViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val portfolioState by walletViewModel.portfolio.collectAsState()
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Header(walletViewModel)
                        when (portfolioState) {
                            is ViewState.Success -> {
                                TotalPrice(portfolioState.data!!)
                                CoinContent(portfolioState.data!!)
                            }
                            is ViewState.Loading -> {
                            }
                            is ViewState.Error -> {}
                        }
                    }
                }
            }
        }
        walletViewModel.getPortfolio()
    }
}

@Composable
fun Header(walletViewModel: ComposeWalletViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 12.dp, end = 12.dp, top = 20.dp
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val context = LocalContext.current
        Text(
            text = "Portfolio",
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = Typography.titleLarge
        )
        IconButton(
            onClick = {
                walletViewModel.getPortfolio()
            }) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_refresh_24),
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                contentDescription = "refresh data"
            )
        }
        IconButton(
            onClick = {
                Toast.makeText(context, "Night mode clicked", Toast.LENGTH_SHORT)
                    .show()
            }) {
            Icon(
                painter = if (!isSystemInDarkTheme()) painterResource(id = R.drawable.ic_baseline_nights_stay_24)
                else painterResource(id = R.drawable.ic_baseline_wb_sunny_24),
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                contentDescription = "toggle dark mode"
            )
        }
    }
}

@Composable
fun TotalPrice(portfolio: Portfolio) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, top = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val totalPrice by remember { mutableStateOf(portfolio.totalPrice) }
        val totalPriceAnim by animateFloatAsState(
            targetValue = totalPrice.toFloat(), animationSpec = tween(
                durationMillis = 2000,
                easing = FastOutSlowInEasing
            )
        )

        Text(
            text = String.format("$%.2f", totalPriceAnim),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = Typography.titleMedium,
            maxLines = 1,
        )

        val percentFormat = DecimalFormat("0.##'%'")
        percentFormat.roundingMode = RoundingMode.CEILING
        Text(
            text = percentFormat.format(portfolio.totalPercentChange24h),
            color = if (portfolio.totalPercentChange24h > 0) Color(android.graphics.Color.GREEN)
            else Color(android.graphics.Color.RED),
            style = Typography.titleMedium,
            maxLines = 1,
        )
    }
}

@Composable
fun CoinContent(portfolio: Portfolio) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 15.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            portfolio.coinList.size,
            itemContent = {
                CoinItem(coin = portfolio.coinList[it])
            }
        )
    }
}

@Composable
fun CoinItem(coin: DisplayableCoin) {
    Column {
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
                style = Typography.bodyMedium
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

            val percentFormat = DecimalFormat("0.##'%'")
            percentFormat.roundingMode = RoundingMode.CEILING
            Text(
                text = percentFormat.format(coin.percentChange24h),
                style = Typography.bodySmall,
                color = if (coin.percentChange24h > 0) Color(android.graphics.Color.GREEN)
                else Color(android.graphics.Color.RED)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppTheme {
        Surface(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .height(IntrinsicSize.Max),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
        }
    }
}