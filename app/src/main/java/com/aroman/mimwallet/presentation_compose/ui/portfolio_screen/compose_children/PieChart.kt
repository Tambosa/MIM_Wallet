package com.aroman.mimwallet.presentation_compose.ui.portfolio_screen.compose_children

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.dp
import com.aroman.mimwallet.common.ViewState
import com.aroman.mimwallet.domain.model.DisplayableCoin
import com.aroman.mimwallet.domain.model.Portfolio
import com.aroman.mimwallet.presentation_compose.ui.theme.Typography
import com.aroman.mimwallet.utils.pie_chart_view.PieData
import com.aroman.mimwallet.utils.pie_chart_view.PieSlice

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