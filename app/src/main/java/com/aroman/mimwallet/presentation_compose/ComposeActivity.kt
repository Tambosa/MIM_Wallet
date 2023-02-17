package com.aroman.mimwallet.presentation_compose

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.*
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
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
                        Header(walletViewModel = walletViewModel, portfolioState = portfolioState)
//                        TotalPrice(portfolioState = portfolioState)
//                        CoinContent(portfolioState = portfolioState)
//                        LoadingAnimation()
                    }
                }
            }
        }
        walletViewModel.getPortfolio()
    }
}

@Composable
fun Header(walletViewModel: ComposeWalletViewModel, portfolioState: ViewState<Portfolio>) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 12.dp, end = 12.dp, top = 20.dp
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val context = LocalContext.current
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

//@Composable
//fun TotalPrice(portfolioState: ViewState<Portfolio>) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(start = 12.dp, end = 12.dp, top = 20.dp),
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        val totalPrice by remember { mutableStateOf(portfolioState.totalPrice) }
//        val totalPriceAnim by animateFloatAsState(
//            targetValue = totalPrice.toFloat(), animationSpec = tween(
//                durationMillis = 2000,
//                easing = FastOutSlowInEasing
//            )
//        )
//        Text(
//            text = String.format("$%.2f", totalPriceAnim),
//            color = MaterialTheme.colorScheme.onPrimaryContainer,
//            style = Typography.titleMedium,
//            maxLines = 1,
//        )
//        val percentFormat = DecimalFormat("0.##'%'")
//        percentFormat.roundingMode = RoundingMode.CEILING
//        Text(
//            text = percentFormat.format(portfolioState.totalPercentChange24h),
//            color = if (portfolioState.totalPercentChange24h > 0) Color(android.graphics.Color.GREEN)
//            else Color(android.graphics.Color.RED),
//            style = Typography.titleMedium,
//            maxLines = 1,
//        )
//    }
//}

@Composable
fun LoadingAnimation(
    indicatorSize: Dp = 50.dp,
    circleColors: List<Color> = listOf(
        Color(0xFF5851D8),
        Color(0xFF833AB4),
        Color(0xFFC13584),
        Color(0xFFE1306C),
        Color(0xFFFD1D1D),
        Color(0xFFF56040),
        Color(0xFFF77737),
        Color(0xFFFCAF45),
        Color(0xFFFFDC80),
        Color(0xFF5851D8)
    ),
    animationDuration: Int = 360
) {
    val infiniteTransition = rememberInfiniteTransition()
    val rotateAnimation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = animationDuration,
                easing = LinearEasing
            )
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
                .size(size = indicatorSize)
                .rotate(degrees = rotateAnimation)
                .border(
                    width = 4.dp,
                    brush = Brush.sweepGradient(circleColors),
                    shape = CircleShape
                ),
            progress = 1f,
            strokeWidth = 1.dp,
            color = MaterialTheme.colorScheme.primaryContainer // Set background color
        )
    }
}

//@Composable
//fun CoinContent(portfolioState: ViewState<Portfolio>) {
//    LazyColumn(
//        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 15.dp),
//        verticalArrangement = Arrangement.spacedBy(8.dp),
//    ) {
//        items(
//            portfolioState.coinList.size,
//            itemContent = {
//                DisplayableCoinItem(coin = portfolioState.coinList[it])
//                if (portfolioState.coinList.isEmpty()) {
//                    DisplayableHint()
//                    DisplayableAddCoin()
//                } else if (it == portfolioState.coinList.size - 1) {
//                    DisplayableAddCoin()
//                }
//            }
//        )
//    }
//}

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
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                contentDescription = "add coin"
            )
        }
    }
}


@Composable
fun DisplayableCoinItem(coin: DisplayableCoin) {
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
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
        }
    }
}