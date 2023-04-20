package com.aroman.mimwallet.presentation.ui.portfolio.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.aroman.mimwallet.domain.model.DisplayableCoin
import com.aroman.mimwallet.presentation.ui.theme.Typography
import com.aroman.mimwallet.presentation.ui.viewmodels.PortfolioViewModel
import java.math.RoundingMode
import java.text.DecimalFormat

@Composable
fun DisplayableCoinItem(
    coin: DisplayableCoin,
    timePeriodSelection: PortfolioViewModel.TimePeriod,
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
                PortfolioViewModel.TimePeriod.ONE_HOUR -> coin.percentChange1h
                PortfolioViewModel.TimePeriod.TWENTY_FOUR_HOURS -> coin.percentChange24h
                PortfolioViewModel.TimePeriod.SEVEN_DAYS -> coin.percentChange7d
                PortfolioViewModel.TimePeriod.THIRTY_DAYS -> coin.percentChange30d
                PortfolioViewModel.TimePeriod.SIXTY_DAYS -> coin.percentChange60d
                PortfolioViewModel.TimePeriod.NINETY_DAYS -> coin.percentChange90d
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