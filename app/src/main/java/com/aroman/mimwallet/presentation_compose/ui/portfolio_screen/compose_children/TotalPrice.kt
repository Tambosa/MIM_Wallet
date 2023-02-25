package com.aroman.mimwallet.presentation_compose.ui.portfolio_screen.compose_children

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.aroman.mimwallet.domain.model.Portfolio
import com.aroman.mimwallet.presentation_compose.ui.theme.Typography
import com.aroman.mimwallet.presentation_compose.ui.viewmodels.ComposeWalletViewModel
import java.math.RoundingMode
import java.text.DecimalFormat

@Composable
fun TotalPrice(
    portfolio: Portfolio,
    timePeriodSelection: ComposeWalletViewModel.TimePeriod,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 12.dp, top = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        var totalPrice by remember { mutableStateOf(0.0) }
        val totalPriceAnim = remember { Animatable(0f) }
        totalPrice = portfolio.totalPrice
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
        totalPercent = with(portfolio) {
            when (timePeriodSelection) {
                ComposeWalletViewModel.TimePeriod.ONE_HOUR -> totalPercentChange1h
                ComposeWalletViewModel.TimePeriod.TWENTY_FOUR_HOURS -> totalPercentChange24h
                ComposeWalletViewModel.TimePeriod.SEVEN_DAYS -> totalPercentChange7d
                ComposeWalletViewModel.TimePeriod.THIRTY_DAYS -> totalPercentChange30d
                ComposeWalletViewModel.TimePeriod.SIXTY_DAYS -> totalPercentChange60d
                ComposeWalletViewModel.TimePeriod.NINETY_DAYS -> totalPercentChange90d
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