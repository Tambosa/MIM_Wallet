package com.aroman.mimwallet.presentation_compose

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
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
    private val themeViewModel by viewModels<ComposeThemeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val portfolioState by walletViewModel.portfolio.collectAsState()
            val isDarkTheme = themeViewModel.isDarkTheme.collectAsState(initial = true)
            val timePeriodSelection by walletViewModel.timePeriod.collectAsState()
            AppTheme(useDarkTheme = isDarkTheme.value) {
                PortfolioScreen(
                    portfolioState,
                    timePeriodSelection
                )
            }
        }
        walletViewModel.getPortfolio()
    }

    @Composable
    fun PortfolioScreen(
        portfolioState: ViewState<Portfolio>,
        timePeriodSelection: ComposeWalletViewModel.TimePeriod
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Header(
                    themeViewModel = themeViewModel,
                    walletViewModel = walletViewModel,
                    portfolioState = portfolioState,
                )
                TotalPrice(
                    portfolioState = portfolioState,
                    timePeriodSelection = timePeriodSelection
                )
                TimePeriodSelection(
                    walletViewModel = walletViewModel,
                    timePeriodSelection = timePeriodSelection
                )
                CoinContent(
                    portfolioState = portfolioState,
                    timePeriodSelection = timePeriodSelection
                )
            }
        }
    }
}

@Composable
fun Header(
    themeViewModel: ComposeThemeViewModel,
    walletViewModel: ComposeWalletViewModel,
    portfolioState: ViewState<Portfolio>,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 12.dp, end = 12.dp, top = 20.dp
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        var isLoading by remember { mutableStateOf(true) }
        isLoading = portfolioState is ViewState.Loading

        val angle by rememberInfiniteTransition().animateFloat(
            initialValue = 0F,
            targetValue = 360F,
            animationSpec = infiniteRepeatable(
                animation = tween(500, easing = LinearEasing)
            )
        )
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
                contentDescription = "refresh data",
                modifier = Modifier.graphicsLayer { if (isLoading) rotationZ = angle }
            )
        }
        IconButton(
            onClick = {
                themeViewModel.inverseTheme()
            }) {
            Icon(
                painter = if (themeViewModel.isDarkTheme.collectAsState().value) painterResource(id = R.drawable.ic_baseline_nights_stay_24)
                else painterResource(id = R.drawable.ic_baseline_wb_sunny_24),
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = "toggle dark mode"
            )
        }
    }
}

@Composable
fun TotalPrice(
    portfolioState: ViewState<Portfolio>,
    timePeriodSelection: ComposeWalletViewModel.TimePeriod
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, top = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        var totalPrice by remember { mutableStateOf(0.0) }
        //TODO why !!
        if (portfolioState is ViewState.Success) {
            totalPrice = portfolioState.data!!.totalPrice
        }
        val totalPriceAnim by animateFloatAsState(
            targetValue = totalPrice.toFloat(),
            animationSpec = tween(1200, 0, FastOutSlowInEasing)
        )
        Text(
            text = String.format("$%.2f", totalPriceAnim),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = Typography.titleMedium,
            maxLines = 1,
        )

        var totalPercent by remember { mutableStateOf(0.0) }
        if (portfolioState is ViewState.Success) {
            totalPercent = when (timePeriodSelection) {
                ComposeWalletViewModel.TimePeriod.ONE_HOUR -> portfolioState.data!!.totalPercentChange1h
                ComposeWalletViewModel.TimePeriod.TWENTY_FOUR_HOURS -> portfolioState.data!!.totalPercentChange24h
                ComposeWalletViewModel.TimePeriod.SEVEN_DAYS -> portfolioState.data!!.totalPercentChange7d
                ComposeWalletViewModel.TimePeriod.THIRTY_DAYS -> portfolioState.data!!.totalPercentChange30d
                ComposeWalletViewModel.TimePeriod.SIXTY_DAYS -> portfolioState.data!!.totalPercentChange60d
                ComposeWalletViewModel.TimePeriod.NINETY_DAYS -> portfolioState.data!!.totalPercentChange90d
            }
        }
        val totalPercentAnim by animateFloatAsState(
            targetValue = totalPercent.toFloat(),
            animationSpec = tween(1000, 0, FastOutSlowInEasing)
        )
        val percentFormat = DecimalFormat("0.##'%'").apply {
            roundingMode = RoundingMode.CEILING
        }
        Text(
            text = percentFormat.format(totalPercentAnim),
            color = if (totalPercent > 0) Color(android.graphics.Color.GREEN)
            else Color(android.graphics.Color.RED),
            style = Typography.titleMedium,
            maxLines = 1,
        )
    }
}


@Composable
fun TimePeriodSelection(
    walletViewModel: ComposeWalletViewModel,
    timePeriodSelection: ComposeWalletViewModel.TimePeriod
) {
    val timePeriodList = ComposeWalletViewModel.TimePeriod.values().asList()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            items(
                count = ComposeWalletViewModel.TimePeriod.values().size,
                itemContent = {
                    //TODO it???
                    val index = it
                    TimePeriodChip(
                        name = timePeriodList[index].value,
                        isSelected = timePeriodList[index] == timePeriodSelection,
                        onSelectionChanged = { walletViewModel.setTimePeriod(timePeriodList[index]) }
                    )
                }
            )
        }

    }
}

@Composable
fun TimePeriodChip(
    name: String = "Chip",
    isSelected: Boolean = false,
    onSelectionChanged: (String) -> Unit = {},
) {
    Surface(
        modifier = Modifier.padding(4.dp),
        shape = MaterialTheme.shapes.medium,
        color = if (isSelected) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.onPrimary
    ) {
        Row(modifier = Modifier
            .toggleable(
                value = isSelected,
                onValueChange = {
                    onSelectionChanged(name)
                }
            )
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun CoinContent(
    portfolioState: ViewState<Portfolio>,
    timePeriodSelection: ComposeWalletViewModel.TimePeriod
) {
    var coinList by remember { mutableStateOf(listOf<DisplayableCoin>()) }
    if (portfolioState is ViewState.Success) {
        coinList = portfolioState.data!!.coinList
    }
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 15.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        if (coinList.isEmpty()) {
            items(1,
                itemContent = {
                    DisplayableHint()
                    DisplayableAddCoin()
                })
        }
        if (coinList.isNotEmpty()) {
            items(
                coinList.size,
                itemContent = {
                    DisplayableCoinItem(
                        coin = coinList[it],
                        timePeriodSelection = timePeriodSelection
                    )
                    if (it == coinList.size - 1) {
                        DisplayableAddCoin()
                    }
                }
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
fun DisplayableAddCoin() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        val context = LocalContext.current
        IconButton(
            onClick = {
                Toast.makeText(context, "add item", Toast.LENGTH_SHORT).show()
            }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_add_circle_outline_24),
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = "add coin"
            )
        }
    }
}


@Composable
fun DisplayableCoinItem(
    coin: DisplayableCoin,
    timePeriodSelection: ComposeWalletViewModel.TimePeriod
) {
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
            val percentFormat = DecimalFormat("0.##'%'").apply {
                roundingMode = RoundingMode.CEILING
            }
            val percentChange = when (timePeriodSelection) {
                ComposeWalletViewModel.TimePeriod.ONE_HOUR -> coin.percentChange1h
                ComposeWalletViewModel.TimePeriod.TWENTY_FOUR_HOURS -> coin.percentChange24h
                ComposeWalletViewModel.TimePeriod.SEVEN_DAYS -> coin.percentChange7d
                ComposeWalletViewModel.TimePeriod.THIRTY_DAYS -> coin.percentChange30d
                ComposeWalletViewModel.TimePeriod.SIXTY_DAYS -> coin.percentChange60d
                ComposeWalletViewModel.TimePeriod.NINETY_DAYS -> coin.percentChange90d
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

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppTheme(false) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
        }
    }
}