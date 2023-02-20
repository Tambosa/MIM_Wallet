package com.aroman.mimwallet.presentation_compose

import android.animation.ObjectAnimator
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.graphics.applyCanvas
import com.aroman.mimwallet.R
import com.aroman.mimwallet.common.ViewState
import com.aroman.mimwallet.domain.model.DisplayableCoin
import com.aroman.mimwallet.domain.model.Portfolio
import com.aroman.mimwallet.presentation_compose.ComposeWalletViewModel.TimePeriod
import com.aroman.mimwallet.presentation_compose.ui.compose_views.CircularChip
import com.aroman.mimwallet.presentation_compose.ui.compose_views.DisplayableCoinItem
import com.aroman.mimwallet.presentation_compose.ui.compose_views.DisplayableHint
import com.aroman.mimwallet.presentation_compose.ui.compose_views.DisplayableInsertCoin
import com.aroman.mimwallet.presentation_compose.ui.theme.AppTheme
import com.aroman.mimwallet.presentation_compose.ui.theme.Typography
import com.aroman.mimwallet.utils.disableTouch
import com.aroman.mimwallet.utils.enableTouch
import com.aroman.mimwallet.utils.pie_chart_view.PieData
import com.aroman.mimwallet.utils.pie_chart_view.PieSlice
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
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        val view = LocalView.current
                        Header(
                            themeViewModel = themeViewModel,
                            walletViewModel = walletViewModel,
                            portfolioState = portfolioState,
                            onThemeChange = { setScreenShot(view) }
                        )
                        PieChart(portfolioState = portfolioState)
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
        walletViewModel.getPortfolio()
    }

    private fun setScreenShot(view: View) {
        val bmp = Bitmap.createBitmap(
            view.width, view.height,
            Bitmap.Config.ARGB_8888
        ).applyCanvas {
            view.draw(this)
        }
        view.foreground = BitmapDrawable(resources, bmp)

        ObjectAnimator.ofInt(view.foreground, "alpha", 255, 0).apply {
            duration = 800
            doOnStart { this@ComposeActivity.disableTouch() }
            doOnEnd {
                this@ComposeActivity.enableTouch()
                view.foreground = null
            }
        }.start()
    }
}

@Composable
fun PieChart(
    portfolioState: ViewState<Portfolio>
) {
    val pieData = PieData()
    var coinList by remember { mutableStateOf(listOf<DisplayableCoin>()) }
    if (portfolioState is ViewState.Success) {
        coinList = portfolioState.successData.coinList.sortedWith(compareBy { it.price * it.count })
    }

    if (coinList.isNotEmpty()) {
        for (coin in coinList) {
            pieData.add(coin.symbol, coin.price * coin.count)
        }
        var lastAngle = 270f
        //set pie slice dimens
        pieData.pieSlices.forEach {
            it.value.startAngle = lastAngle
            it.value.sweepAngle = (((it.value.value / pieData.totalValue)) * 360f).toFloat()
            lastAngle += it.value.sweepAngle
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Canvas(
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp)
            ) {
                pieData.pieSlices.forEach {
                    drawArc(
                        color = it.value.color,
                        startAngle = it.value.startAngle,
                        sweepAngle = it.value.sweepAngle,
                        useCenter = true
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 20.dp)
        ) {
            val pieSlices = mutableListOf<Pair<String, PieSlice>>()
            pieSlices.addAll(pieData.pieSlices.toList())
            if (pieSlices.size >= 5) {
                for (i in 0..4) {
                    PieHint(pieSlice = pieSlices[i].second)
                }
            } else {
                pieData.pieSlices.forEach {
                    PieHint(it.value)
                }
            }
        }
    }
}

@Composable
fun PieHint(pieSlice: PieSlice) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, top = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Canvas(modifier = Modifier) {
            drawCircle(pieSlice.color, radius = 20f, center = Offset(0f, 10.dp.toPx()))
        }
        Text(text = pieSlice.name, style = Typography.bodySmall)
    }
}

@Composable
fun Header(
    themeViewModel: ComposeThemeViewModel,
    walletViewModel: ComposeWalletViewModel,
    portfolioState: ViewState<Portfolio>,
    onThemeChange: () -> Unit,
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
                onThemeChange()
            }) {
            Icon(
                painter = if (themeViewModel.isDarkTheme.collectAsState().value) painterResource(id = R.drawable.ic_baseline_nights_stay_24)
                else painterResource(id = R.drawable.ic_baseline_wb_sunny_24),
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = "toggle dark mode",
            )
        }
    }
}

@Composable
fun TotalPrice(
    portfolioState: ViewState<Portfolio>,
    timePeriodSelection: TimePeriod,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, top = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        var totalPrice by remember { mutableStateOf(0.0) }
        val totalPriceAnim = remember { Animatable(0f) }
        if (portfolioState is ViewState.Success) {
            totalPrice = portfolioState.successData.totalPrice
        }
        LaunchedEffect(totalPrice) {
            totalPriceAnim.animateTo(totalPrice.toFloat())
        }
        Text(
            text = String.format("$%.2f", totalPriceAnim.value),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            style = Typography.titleMedium,
            maxLines = 1,
        )

        var totalPercent by remember { mutableStateOf(0.0) }
        val totalPercentAnim = remember { Animatable(0f) }
        if (portfolioState is ViewState.Success) {
            totalPercent = with(portfolioState.successData) {
                when (timePeriodSelection) {
                    TimePeriod.ONE_HOUR -> totalPercentChange1h
                    TimePeriod.TWENTY_FOUR_HOURS -> totalPercentChange24h
                    TimePeriod.SEVEN_DAYS -> totalPercentChange7d
                    TimePeriod.THIRTY_DAYS -> totalPercentChange30d
                    TimePeriod.SIXTY_DAYS -> totalPercentChange60d
                    TimePeriod.NINETY_DAYS -> totalPercentChange90d
                }
            }
        }
        LaunchedEffect(totalPercent) {
            totalPercentAnim.animateTo(totalPercent.toFloat())
        }
        val percentFormat = DecimalFormat("0.##'%'").apply {
            roundingMode = RoundingMode.CEILING
        }
        Text(
            text = percentFormat.format(totalPercentAnim.value),
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
    timePeriodSelection: TimePeriod
) {
    val timePeriodList = TimePeriod.values().asList()
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
                count = TimePeriod.values().size,
                itemContent = { index ->
                    CircularChip(
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
fun CoinContent(
    portfolioState: ViewState<Portfolio>,
    timePeriodSelection: TimePeriod
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
                    DisplayableInsertCoin()
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
                        DisplayableInsertCoin()
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppTheme(false) {
    }
}